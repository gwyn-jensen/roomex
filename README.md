# XSLT Transformation

## The Brief

Build an executable .JAR that will process the input XML file and return the output XML.

## Execution

To execute the application run the following commands from the project root directory.
```shell
mvn clean install
java -jar ./target/roomex-1.0-SNAPSHOT.jar "input.xml" "output.xml"
```
You can choose any name you like for the output.xml file.
If you choose the input file "test.xml", you will get an error reporting that no stylesheet exists for that input.
If you choose any other name for the input, you will get a FileNotFoundException reporting that the requested input resource does not exist.

## Test Execution

To execute all the tests, both unit tests and acceptance tests; run the following in the project root dir.
```shell
mvn clean test
```
Or execute the tests from your IDE

## Test Coverage

I have provided unit test coverage for most scenarios but stopped when it started taking too long and I felt I had already demonstrated my approach sufficiently.

Acceptance tests cover the happy path and the two main unhappy paths where requetsed resources do not exist.

## Possible Improvements

1.  The major issue I would still love to solve is how to fail XSLT processing when one of the java extension functions throws an exception.
Currently the processing would just fail silently and produce a part written output document. In order to work around this I just returned a default value
from the only java function that was throwing an exception, instead of throwing the exception.
2.  With exceptions coming from failed transformations there would be a number of other acceptance tests we could write, like what happens when an extension function fails,
or how do we deal with badly-formed input xml.
3.  I didn't implement a logging solution. I just used System.out when I wanted to see some output. This was just to save time.
4.  Because I didn't implement logging my top level exception handling is very naive. I just log the error message and wrap any checked exception in a RuntimeException.
Again, just a timing issue.
