# Group MapReduce project

This Maven project uses Hadoop 3.5.0 and JDK 17. The jobs run in local mode using the sample input in this directory.

1. On Windows, point `HADOOP_HOME` to your Hadoop 3.5.0 installation and add its `bin` directory to `PATH`. The directory needs `winutils.exe` and `hadoop.dll`. Start Eclipse with that environment.
2. In Eclipse, choose **File > Import > Maven > Existing Maven Projects** and select this directory. Use JDK 17 as the workspace JDK and keep the imported project name `team-mapreduce` so the launch configurations can find it.
3. Open **Run > Run Configurations > Java Application** and run the configurations in `launches/`. They use the project as their working directory, write console logs to `logs/`, and write results to `output/eclipse/`.

| Configuration | Job | Input | Output directory |
| --- | --- | --- | --- |
| 01 | `FriendCount` | `input/friendcount` | `output/eclipse/friendcount` |
| 02 | `WordLength` | `input/wordlength` | `output/eclipse/wordlength` |
| 03 | `WordLength` | `input/wordlength10` | `output/eclipse/wordlength10` |
| 04 | `WordLength` | `input/boundaries` | `output/eclipse/boundaries` |
| 05 | `WordLength` | `input/separators` | `output/eclipse/separators` |
| 06 | `InvertedIndex` | `input/invertedindex` | `output/eclipse/invertedindex` |

After all six jobs finish, run the verifier from this project directory with Python 3.

```sh
python scripts/verify_results.py output/eclipse
python scripts/test_verifier.py
```

For another run, choose a fresh output parent such as `output/run02` for all six cases and pass that parent to the verifier. Hadoop requires each job's output directory to be absent before the job starts.
