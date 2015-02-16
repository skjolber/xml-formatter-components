# xml-formatter-components-mule-cxf-example
Mule ESB logging interceptors for CXF example project. 

The project exposes a simple SOAP service.

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
Deploy resulting package on Mule ESB. Then access the [WSDL] at

	http://localhost:50000/services/logger/message?wsdl

and observe log file xmlformatter.log output in Mule log directory using command

    tail -f logs/xmlformatter.log -n 200
    
    
## History
- [1.0.0]: Initial release.


[greenbird]:           http://greenbird.com/
[issue-tracker]:       https://github.com/greenbird/xml-formatter-components/issues
[Apache 2.0]:          http://www.apache.org/licenses/LICENSE-2.0.html
[projects]:            http://greenbird.github.io/
[Maven]:               http://maven.apache.org/
[build-badge]:         https://build.greenbird.com/job/xml-formatter-components/badge/icon
[build-link]:          https://build.greenbird.com/job/xml-formatter-components/
[WSDL]:				   http://localhost:50000/services/logger/message?wsdl
