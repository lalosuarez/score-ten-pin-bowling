#### Ten Pin Bowling Score
Command line program that calculates the scores for a Ten Pin Bowling Game bases on a .tsv file

##### System Requirements
1. [Java 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
2. [Apache Maven](https://maven.apache.org/)


##### How to build
From command line execute:
```sh
mvn clean package
```

##### How to run
From command line execute:
```sh
java -jar target/score-ten-pin-bowling-1.0-jar-with-dependencies.jar "file-samples/score.tsv"
```

As you can see it takes the *"file path + name"* as a command line argument, if you don't provide any
it will take the default one configured in ***"file-samples/score.tsv"***

```sh
java -jar target/score-ten-pin-bowling-1.0-jar-with-dependencies.jar
```

***"file-samples"*** dir contains a few examples of files that you can play with,
for example: invalid content, invalid format, a perfect game etc.

