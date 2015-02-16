# xml-formatter-components-jax-rs-example
Example project for xml-formatter JAX-RS component.

This project is part of the [greenbird] Open Source Java [projects].

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].

[![Build Status][build-badge]][build-link]

## Table of contents

- [License](#license)
- [Obtain](#obtain)
- [Usage](#usage)
- [History](#history)

## License
[Apache 2.0]

## Obtain
The project is based on [Maven] and can be built using the command

	mvn clean package

# Usage
Deploy resulting war package on Apache Tomcat version 7 or any other suitable container. Then access

	http://localhost:8080/xml-formatter-components-jax-rs-example/

### Details
The project contains a single JAX-RS [resource] class and a welcome page.

## History
- [1.0.0]: Initial release.


[greenbird]:           http://greenbird.com/
[issue-tracker]:       https://github.com/greenbird/xml-formatter-components/issues
[Apache 2.0]:          http://www.apache.org/licenses/LICENSE-2.0.html
[projects]:            http://greenbird.github.io/
[Maven]:               http://maven.apache.org/
[build-badge]:         https://build.greenbird.com/job/xml-formatter-components/badge/icon
[build-link]:          https://build.greenbird.com/job/xml-formatter-components/
[resource]:            https://github.com/greenbird/xml-formatter-components/jax-rs-example/src/main/java/com/greenbird/xml/prettyprint/rest/resource/MyResource.java