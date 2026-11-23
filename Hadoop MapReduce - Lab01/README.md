# Hadoop MapReduce - Lab01

One Maven project contains the three lab tasks in package `de.tud.stg`.

| Task | Java file | Original source | Input |
| --- | --- | --- | --- |
| 2 — Friend count | `src/main/java/de/tud/stg/FriendCount.java` | `hadoop/hadoop` | `input/friendcount` |
| 3 — Word length | `src/main/java/de/tud/stg/WordLength.java` | `Group_lab1/MapReduce_Eclipse` | `input/wordlength` |
| 4 — Inverted index | `src/main/java/de/tud/stg/Comprehension.java` | `source_code_Task4/hadoop` | `input/invertedindex` |

Each Java file is copied byte for byte from its original source. The shared Maven configuration uses JDK 17 and Hadoop 3.5.0 from the group project.

## Build and run

From the repository root, build with Maven 3.9 or newer.

```sh
mvn -f "Hadoop MapReduce - Lab01/pom.xml" package
```

In VS Code, open the repository root, let the Java extensions import the Maven project, and select **Task 2 - FriendCount**, **Task 3 - WordLength**, or **Task 4 - Inverted Index** in **Run and Debug**.

In Eclipse, import this folder with **Maven > Existing Maven Projects** and keep the project name `hadoop-mapreduce-lab01`. Use JDK 17 and run the corresponding configuration in `launches/`.

Each launch uses this lab folder as its working directory and writes to `output/run01/<case>`. On Windows, configure `HADOOP_HOME` and add its `bin` directory to `PATH` before starting the IDE. Use a fresh output path when running again.

## Verify results

Task 2 produces Jim 3, Joe 1, Kai 1, Lin 2, and Sue 1. Task 3 produces tiny 4, small 6, medium 5, and big 5. Task 4 produces the following filename occurrences, whose order within a row may vary.

| Word | Filenames |
| --- | --- |
| Bye | file01.txt |
| Goodbye | file02.txt |
| Hadoop | file02.txt,file02.txt |
| Hello | file01.txt,file02.txt |
| World | file01.txt,file01.txt |

Additional Task 3 fixtures are in `input/wordlength10`, `input/boundaries`, and `input/separators`. Run `WordLength` with each input and the corresponding `output/run01/<case>` path. After all six cases finish, run the existing verifier from this lab folder.

```sh
python scripts/verify_results.py output/run01
python scripts/test_verifier.py
```
