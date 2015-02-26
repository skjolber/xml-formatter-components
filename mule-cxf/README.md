# xml-formatter-components-mule-cxf
Mule ESB logging interceptors for CXF which extract message unique and correlation id. 

Note that the interceptors found in [xml-formatter-components-cxf] also can be directly applied in Mule.

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
The project is based on [Maven] and is available on the central Maven repository.

Example dependency config:

```xml
<dependency>
    <groupId>com.greenbird.xml-formatter</groupId>
    <artifactId>xml-formatter-components-mule-cxf</artifactId>
    <version>1.0.0</version>
</dependency>
```

You can also [download] the jar directly if you need too.

Snapshot builds are available from the Sonatype OSS [snapshot repository].
# Usage
Add input- and/or output interceptors for logging. For example

```xml
<http:inbound-endpoint address="${inbound.url}" exchange-pattern="request-response">
    <cxf:proxy-service wsdlLocation="..." namespace="..." service="...">
        <cxf:inInterceptors>
            <spring:bean class="com.greenbird.xmlformatter.mule.cxf.XMLLoggingInInterceptor">
                <spring:property name="name" value="MyServiceReceived"/>
            </spring:bean>
        </cxf:inInterceptors>
        <cxf:outInterceptors>
            <spring:bean class="com.greenbird.xmlformatter.mule.cxf.XMLLoggingOutInterceptor">
                <spring:property name="name" value="MyServiceSent"/>
            </spring:bean>
        </cxf:outInterceptors>
    </cxf:proxy-service>
</http:inbound-endpoint>
```
                
## Details
Various options can be configured using factories using various spring constructs, for example

```xml
<spring:bean class="com.greenbird.xml.prettyprinter.PrettyPrinterFactory" name="inputPrettyPrinterFactory">
	<!-- configure properties -->
</spring:bean>    	

<spring:bean class="com.greenbird.xml.prettyprinter.PrettyPrinterFactory" name="outputPrettyPrinterFactory">
	<!-- configure properties -->
</spring:bean>    	

<flow name="main">
	<http:inbound-endpoint address="${logger.inbound.url}" exchange-pattern="request-response">
        <cxf:proxy-service wsdlLocation="classpath:wsdl/logger.wsdl" namespace="http://xmlns.greenbird.com/schema/logger" service="LoggerService">
            <cxf:inInterceptors>
                <spring:bean class="com.greenbird.xmlformatter.mule.cxf.XMLLoggingInInterceptor">
                    <spring:property name="name" value="LoggerServiceReceived"/>
                    <spring:property name="prettyPrinter" value="#{inputPrettyPrinterFactory.newPrettyPrinter()}"/>
                </spring:bean>
            </cxf:inInterceptors>
            <cxf:outInterceptors>
                <spring:bean class="com.greenbird.xmlformatter.mule.cxf.XMLLoggingOutInterceptor">
                    <spring:property name="name" value="LoggerServiceSent"/>
                    <spring:property name="prettyPrinter" value="#{outputPrettyPrinterFactory.newPrettyPrinter()}"/>
                </spring:bean>
            </cxf:outInterceptors>
        </cxf:proxy-service>
	</http:inbound-endpoint>
	
	<mule-xml:xslt-transformer xsl-file="xsl/logger.xsl" returnClass="java.lang.String">
		<mule-xml:context-property key="statusCode" value="1"/>
	</mule-xml:xslt-transformer>
</flow>
```

For additional Spring configuration, see the [Spring Cheat Sheet].

See the [xml-formatter-components-mule-cxf-example] submodule for a working example.
## History
- [1.0.0]: Initial release.


[greenbird]:           http://greenbird.com/
[issue-tracker]:       https://github.com/greenbird/xml-formatter-components/issues
[Apache 2.0]:          http://www.apache.org/licenses/LICENSE-2.0.html
[projects]:            http://greenbird.github.io/
[Maven]:               http://maven.apache.org/
[download]:            http://search.maven.org/#search|ga|1|xml-formatter-components
[build-badge]:         https://build.greenbird.com/job/xml-formatter-components/badge/icon
[build-link]:          https://build.greenbird.com/job/xml-formatter-components/
[snapshot repository]: https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/xml-formatter-components
[xml-formatter-components-mule-cxf-example]: ./../mule-cxf-example
[Spring Cheat Sheet]:  ./../spring.cheat.sheet.md
[xml-formatter-components-cxf]: ./../cxf
