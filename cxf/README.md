# xml-formatter-components-cxf
Pretty-printing CXF logging feature implementation using [xml-formatter].

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
    <artifactId>xml-formatter-components-cxf</artifactId>
    <version>1.0.0</version>
</dependency>
```

You can also [download] the jar directly if you need too.

Snapshot builds are available from the Sonatype OSS [snapshot repository].
# Usage
Add 

	 com.greenbird.xmlformatter.cxf.LoggingFeature

as a feature. Refer to [CXF features documentation] for details.

## Details
The component includes two additional interceptors intended for subclassing:

	com.greenbird.xmlformatter.cxf.XMLLoggingOutInterceptor

and

	com.greenbird.xmlformatter.cxf.XMLLoggingInInterceptor

These simplify the process of logging only specific request attributes, like message id and such, and are generally less verbose than the default CXF interceptor.

For Spring configuration, see the [Spring Cheat Sheet].
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
[snapshot repository]: 	https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/xml-formatter/xml-formatter-components
[CXF features documentation]: http://cxf.apache.org/docs/features.html
[xml-formatter]:	   https://github.com/greenbird/xml-formatter-core
[Spring Cheat Sheet]:  ./../spring.cheat.sheet.md