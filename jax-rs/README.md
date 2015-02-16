# xml-formatter-components-jax-rs
JAX-RS annotation for pretty-printing of XML in RESTful services.

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
    <artifactId>xml-formatter-components-jax-rs</artifactId>
    <version>1.0.0</version>
</dependency>
```

You can also [download] the jar directly if you need too.

Snapshot builds are available from the Sonatype OSS [snapshot repository].
# Usage
Use the `PrettyPrint` annotation to enable logging of XML in incoming and/or outgoing request bodies. The body `MediaType` main type must be either `text` or `application` and the subtype must contain `xml`. Requests are logged with INFO level. Non-XML requests are logged as text.

## Class annotation
Add the `PrettyPrint` annotation to a class and it applies to all resource methods.

```java
@Path("myResource")
@PrettyPrint
public class MyResource {
    ...
}
```

## Method annotation
Add the `PrettyPrint` annotation to a method.

```java
@Path("myRequestPath")
@Produces("application/xml")
@Consumes("application/xml")
@POST
@PrettyPrint
public MyResponseObject myRequest(MyRequestObject r) { // JAXB-annotated example objects
    ...
}
```

## Details
The `PrettyPrint` annotation supports parameters corresponding to the [PrettyPrinterBuilder] found in [xml-formatter-core].

### Max CDATA node sizes
Configuring

```java
@PrettyPrint(maxCDATANodeLength = 1024, maxTextNodeLength = 1024)
```

yields output like (at a smaller max length)

```xml
<parent>
    <child><![CDATA[QUJDREVGR0hJSktMTU5PUFFSU1...[TRUNCATED BY 46]]]></child>
</parent>
```

for CDATA and correspondingly for text nodes.
### Reformatting of XML within text and/or CDATA nodes
Configuring

```java
@PrettyPrint(prettyPrintCData = true, prettyPrintTextNodes = true)
```

yields output like

```xml
<parent>
    <child><![CDATA[
		<inner>
		    <xml>text</xml>
	    </inner>]]>
    </child>
</parent>
```

for CDATA and correspondingly for text nodes.

### Anonymizing attributes and/or elements
Configuring

```java
@PrettyPrint(anonymizeFilters = {"/parent/child"}) // multiple paths supported
```

results in 

```xml
<parent>
    <child>[*****]</child>
</parent>
```

See below for supported XPath syntax.
### Removing subtrees
Configuring

```java
@PrettyPrint(pruneFilters = {"/parent/child"}) // multiple paths supported
```

results in

```xml
<parent>
	<child>
	    <!-- [SUBTREE REMOVED] -->
    </child>
</parent>
```

See the [implementation-limitations] for supported XPath expressions and various limitations..

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
[xml-formatter-core]:  https://github.com/greenbird/xml-formatter-core
[PrettyPrinterBuilder]: https://github.com/greenbird/xml-formatter-core/blob/master/src/main/java/com/greenbird/xml/prettyprinter/PrettyPrinterBuilder.java
[implementation-limitations]: https://github.com/greenbird/xml-formatter-core/blob/master/README.md#implementation-limitations
