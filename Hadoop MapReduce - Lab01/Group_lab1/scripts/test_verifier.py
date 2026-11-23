"""Regression for false success when Python assertions are optimized away."""
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest


class VerifierCliTests(unittest.TestCase):
    def test_missing_outputs_are_rejected_under_python_optimization(self):
        with tempfile.TemporaryDirectory() as folder:
            result = subprocess.run([sys.executable, '-B', '-O',
                                     str(Path(__file__).with_name('verify_results.py')),
                                     str(Path(folder) / 'missing-outputs')],
                                    capture_output=True, text=True)
        self.assertEqual(result.returncode, 1, result.stdout + result.stderr)
        self.assertIn('0/6 integration cases passed', result.stdout)


if __name__ == '__main__':
    unittest.main()
