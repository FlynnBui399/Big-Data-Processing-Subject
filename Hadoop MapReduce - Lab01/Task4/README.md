# Task 4 - Inverted Index

Imported from `source_code_Task4/hadoop`. This standalone Maven project contains `de.tud.stg.Comprehension`, the Task 4 sample inputs, and the supplied logging configuration. It uses Hadoop 2.7.2 and the Maven project name `task4-invertedindex`.

The mapper emits one `(word, filename)` pair for each occurrence. A single reducer joins filenames with commas, retaining repeated filenames when a word occurs more than once in a file. The combiner is disabled in this implementation.

## Build and run

Use JDK 17 and Maven 3.9 or newer. From the repository root, build with the following command.

```sh
mvn -f "Hadoop MapReduce - Lab01/Task4/pom.xml" package
```

In VS Code, open the repository root, wait for the Java extensions to import the Maven projects, and run **Task 4 - Inverted Index**. It reads `input/invertedindex` and writes to `output/comprehension-vscode`, relative to this project.

In Eclipse, import this directory with **Maven > Existing Maven Projects**, keep the name `task4-invertedindex`, and run `Comprehension.launch`. It uses the project as its working directory and writes to `output/comprehension-eclipse`. Maven includes `src/main/resources/log4j.properties` on the runtime classpath.

On Windows, configure `HADOOP_HOME` and add its `bin` directory to `PATH` before starting the IDE. Hadoop requires a fresh output directory for every run. Change the launch configuration's output argument when running again.

## Expected output

The result appears in `part-r-00000`, accompanied by `_SUCCESS`.

| Word | Filenames |
| --- | --- |
| Bye | file01.txt |
| Goodbye | file02.txt |
| Hadoop | file02.txt,file02.txt |
| Hello | file01.txt,file02.txt |
| World | file01.txt,file01.txt |

Filename order within a row may vary. Check each filename's occurrence count when comparing results.
