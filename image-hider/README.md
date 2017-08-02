## image-hider

This project builds an executable JAR that can be used to hide/recover hidden files (usually archived as a zip) inside a picture (JPG) without affecting the picture.

## Motivation

Hide sensitive data inside pictures so that nobody can find them.
Be careful, if you send the picture via WhatsApp or Facebook the image will be altered and your hidden data will be lost.
If you share it use services that don't alter the picture, example: wetransfer.com

## Installation

There's no reference to external dependency. The Jar can be build with Maven. Tested with Java 7.

mvn install

## API Reference

When running the JAR with wrong arguments the documentation on how to run it will be displayed.

java -jar image-hider.jar

## Tests

Not yet written. MainTest.java will not use JUnit in order to have no dependency at build/run time.

## Contributors

Cosmin

## License

Free to use, change, etc.
