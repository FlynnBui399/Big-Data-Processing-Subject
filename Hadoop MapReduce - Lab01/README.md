# Hadoop MapReduce - Lab01

| Maven project | Hadoop | MapReduce jobs |
| --- | --- | --- |
| [Group_lab1](Group_lab1/) | 3.5.0 | `FriendCount`, `WordLength`, `InvertedIndex` |
| [hadoop](hadoop/) | 2.7.2 | `WordCount`, `Comprehension`, `FriendCount`, `WordLength`, `InvertedIndex` |

Each project has its own `pom.xml`, Java source, and sample inputs. Build them separately with JDK 17 and Maven 3.9 or newer.

Run these commands from the repository root.

```sh
mvn -f "Hadoop MapReduce - Lab01/Group_lab1/pom.xml" package
mvn -f "Hadoop MapReduce - Lab01/hadoop/pom.xml" package
```

The [group project guide](Group_lab1/README.md) covers the Eclipse configurations and the six sample cases. For the `hadoop` examples, launch the chosen class with its input and a fresh output path, relative to the `hadoop` project directory. The supplied inputs are `input/wordcount` for `WordCount` and `Comprehension`, and `input/friendcount`, `input/wordlength`, and `input/invertedindex` for the other three jobs respectively.

To run `hadoop`'s `FriendCount` in VS Code, open the repository root, let the Java extensions import both Maven projects, and select **FriendCount** in **Run and Debug**. The launch writes to `hadoop/output/friendcount` under this lab folder. Use a fresh output path for each run. On Windows, configure `HADOOP_HOME` and add its `bin` directory to `PATH` before launching the IDE.
