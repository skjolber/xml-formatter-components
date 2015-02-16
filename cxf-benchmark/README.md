# xml-formatter-components-cxf-benchmark
Project for benchmarking CXF logging interceptors implemented as SOAP webservices in Mule ESB version 3.4. 

The same service is exposed multiple times with different pretty-printing logging interceptors:

 * xml-formatter interceptors
   * CXF drop-in replacement feature
   * Mule-specific
 * CXF interceptors
   * legacy (Transformer-based)
   * latest (StAX-based)

The webservice itself consists of a single method which allows for any-type content within. So the project can be used to benchmark various types of XML.

This project is part of the [greenbird] Open Source Java [projects].

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].

[![Build Status][build-badge]][build-link]

## License
[Apache 2.0]

## Obtain
The project is based on [Maven] and can be built using the command

	mvn clean package

# Usage
Build and deploy the resulting package on Mule ESB. Then access the [xml-formatter CXF WSDL] at

	http://localhost:50000/services/logger/xmlformatter-cxf

and the directly comparable (same logging output) [CXF WSDL] at

	http://localhost:50002/services/logger/cxf

which does pretty-printing based on a [Transformer]. The latest CXF in git does pretty-printing using StAX, an equivalent implementation is available at 

	http://localhost:50004/services/logger/cxf-stax

as the [CXF StAX WSDL]. The [xml-formatter Mule CXF WSDL] at

	http://localhost:50001/services/logger/xmlformatter-mule-cxf

gives somewhat less logging than the two first endpoints. A [reference endpoint], which does no logging, is available at

	http://localhost:50003/services/logger/without-logging

## Details
Make a SOAP request as follows:

	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:log="http://xmlns.greenbird.com/schema/logger">
		<soapenv:Header />
		<soapenv:Body>
			<log:performLogMessageRequest>
				<!-- your XML here -->
			</log:performLogMessageRequest>
		</soapenv:Body>
	</soapenv:Envelope>

and the service responds with

	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	   <soap:Body>
	      <log:performLogMessageResponse xmlns:log="http://xmlns.greenbird.com/schema/logger">
				<!-- your XML here -->
	      </log:performLogMessageResponse>
	   </soap:Body>
	</soap:Envelope>

See the SoapUI project in the test resources folder for some simple load tests. Note that SoapUI automagically pretty-prints test request responses, and that benchmarking for small payloads is difficult due to millisecond rounding errors.

#### Configure pretty-printing
Open the mule-config.xml file and add/remove PrettyPrinterFactory properties

	<spring:bean class="com.greenbird.xml.prettyprinter.PrettyPrinterFactory" name="prettyPrinterFactory">
		<!-- configure properties -->
		<spring:property name="indentationCharacter" value=" "/>
		<spring:property name="indentationMultiplier" value="2"/>
	</spring:bean>   

to alter the behaviour of the logging for the two endpoints using xml-formatter pretty-printing.
## History
- [1.0.0]: Initial release.


[greenbird]:           			http://greenbird.com/
[issue-tracker]:       			https://github.com/greenbird/greenbird-xml-formatter-components/issues
[Apache 2.0]:          			http://www.apache.org/licenses/LICENSE-2.0.html
[projects]:            			http://greenbird.github.io/
[Maven]:               			http://maven.apache.org/
[download]:            			http://search.maven.org/#search|ga|1|greenbird-xml-formatter-components
[build-badge]:         			https://build.greenbird.com/job/greenbird-xml-formatter-components/badge/icon
[build-link]:          			https://build.greenbird.com/job/greenbird-xml-formatter-components/
[snapshot repository]: 			https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/greenbird-xml-formatter-components
[xml-formatter CXF WSDL]:		http://localhost:50000/services/logger/xmlformatter-cxf?wsdl
[xml-formatter Mule CXF WSDL]:	http://localhost:50001/services/logger/xmlformatter-mule-cxf?wsdl
[CXF WSDL]:						http://localhost:50002/services/logger/cxf?wsdl
[CXF StAX WSDL]:				http://localhost:50004/services/logger/cxf-stax?wsdl
[reference endpoint]:			http://localhost:50003/services/logger/without-logging?wsdl
[Transformer]:                  http://docs.oracle.com/javase/7/docs/api/javax/xml/transform/Transformer.html