# xml-formatter-components-cxf-example
CXF logging interceptors example project implemented using [Apache Camel].

This project is part of the [greenbird] Open Source Java [projects].

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].

[![Build Status][build-badge]][build-link]

## License
[Apache 2.0]

## Obtain
The project is based on [Maven] and can be built using the command

	mvn clean install

# Usage
Start the module by using the command

    mvn camel:run

Then access the [WSDL] at

	http://localhost:50010/services/logger/message?wsdl

and observe log file output using command

    tail -f logs/xmlformatter.log -n 200
    
### Details
For a quickstart, oepn the SoapUI project in the test resources folder. The example request (without whitespace)

	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:log="http://xmlns.greenbird.com/schema/logger">
		<soapenv:Header />
		<soapenv:Body>
			<log:performLogMessageRequest>
				<log:address>my@email.address</log:address>
				<log:subject>My message subject</log:subject>
				<log:body>My message body</log:body>
			</log:performLogMessageRequest>
		</soapenv:Body>
	</soapenv:Envelope>

will be logged as

	2015-01-19 10:44:06,259 INFO  [LoggerPort] Inbound Message
	----------------------------
	ID: 1
	Address: http://localhost:50010/services/logger/message
	Encoding: UTF-8
	Http-Method: POST
	Content-Type: text/xml;charset=UTF-8
	Headers: {accept-encoding=[gzip,deflate], connection=[keep-alive], Content-Length=[379], content-type=[text/xml;charset=UTF-8], Host=[localhost:50010], SOAPAction=[""], User-Agent=[Apache-HttpClient/4.1.1 (java 1.5)]}
	Payload: 
	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:log="http://xmlns.greenbird.com/schema/logger">
	  <soapenv:Header/>
	  <soapenv:Body>
	    <log:performLogMessageRequest>
	      <log:address>my@email.address</log:address>
	      <log:subject>My message subject</log:subject>
	      <log:body>
	        <!-- [SUBTREE REMOVED] -->
	      </log:body>
	    </log:performLogMessageRequest>
	  </soapenv:Body>
	</soapenv:Envelope>

with the default example configuration.
    
#### Maven configuration in Camel
Note that [Apache Camel] has bundled its own CXF packages, so when adding the Maven dependency, exclude excess CXF packages like so:

	<dependency>
		<groupId>com.greenbird.xml-formatter</groupId>
		<artifactId>xml-formatter-components-cxf</artifactId>
		<version>${xmlformatter.version}</version>
		<exclusions>
			<!-- camel has its own implementation of CXF -->
			<exclusion>
				<groupId>org.apache.cxf</groupId>
				<artifactId>cxf-api</artifactId>
			</exclusion>
			<exclusion>
				<groupId>org.apache.cxf</groupId>
				<artifactId>cxf-rt-frontend-jaxws</artifactId>
			</exclusion>
		</exclusions>
	</dependency>

## History
- [1.0.0]: Initial release.


[greenbird]:           http://greenbird.com/
[issue-tracker]:       https://github.com/greenbird/greenbird-xml-formatter-components/issues
[Apache 2.0]:          http://www.apache.org/licenses/LICENSE-2.0.html
[projects]:            http://greenbird.github.io/
[Maven]:               http://maven.apache.org/
[download]:            http://search.maven.org/#search|ga|1|greenbird-xml-formatter-components
[build-badge]:         https://build.greenbird.com/job/greenbird-xml-formatter-components/badge/icon
[build-link]:          https://build.greenbird.com/job/greenbird-xml-formatter-components/
[snapshot repository]: https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/greenbird-xml-formatter-components
[WSDL]:				   http://localhost:50010/services/logger/message?wsdl
[Apache Camel]:        http://camel.apache.org/