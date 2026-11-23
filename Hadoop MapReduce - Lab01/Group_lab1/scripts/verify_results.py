"""Check real Hadoop outputs against hand-counted fixtures (no mocks)."""
import argparse
from collections import Counter
import json
from pathlib import Path
import sys


def require(condition, message):
    if not condition:
        raise ValueError(message)


def validate(output_root):
    expected = json.loads((Path(__file__).resolve().parents[1] / 'expected/results.json').read_text())
    checks = []
    for name, wanted in expected.items():
        folder = output_root / name
        try:
            require((folder / '_SUCCESS').is_file(), 'Hadoop _SUCCESS is missing')
            parts = sorted(folder.glob('part-r-*'))
            require(parts, 'Reducer output is missing')
            rows = [row.split('\t', 1) for p in parts for row in p.read_text(encoding='utf-8').splitlines()]
            require(all(len(row) == 2 for row in rows), 'Output must have two tab-separated fields')
            require(len(rows) == len(wanted), f'Expected {len(wanted)} keys, received {len(rows)}')
            require(len({row[0] for row in rows}) == len(rows), 'Duplicate output key')
            if name == 'invertedindex':
                actual = {k: Counter(v.split(',')) for k, v in rows}
                target = {k: Counter(v) for k, v in wanted.items()}
                require(actual == target, f'Filename multisets differ: {actual}')
            else:
                actual = {k: int(v) for k, v in rows}
                require(actual == wanted, f'Expected {wanted}, received {actual}')
            checks.append({'case': name, 'passed': True, 'output': rows})
        except (AssertionError, OSError, ValueError) as error:
            checks.append({'case': name, 'passed': False, 'error': str(error)})
    return checks


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('output_root', type=Path)
    parser.add_argument('--json', type=Path)
    args = parser.parse_args()
    checks = validate(args.output_root)
    result = {'checks': checks, 'passed': sum(c['passed'] for c in checks), 'total': len(checks)}
    if args.json:
        args.json.write_text(json.dumps(result, indent=2), encoding='utf-8')
    for c in checks:
        print(('PASS' if c['passed'] else 'FAIL') + ' ' + c['case'] + (': ' + c['error'] if not c['passed'] else ''))
    print(f"{result['passed']}/{result['total']} integration cases passed")
    sys.exit(0 if all(c['passed'] for c in checks) else 1)
