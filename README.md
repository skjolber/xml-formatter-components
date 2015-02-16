# xml-formatter-components
Components for logging of XML payloads within popular Java service frameworks:

 * CXF
  * [Logging-feature] drop-in replacement
  * Configurable logging interceptors
  * Mule ESB interceptors
 * JAX-RS
  * [PrettyPrint] annotation for REST services
 
Projects using this library will benefit from:

  * High-performance reformatting of XML
  * Advanced filtering options
    * Max text and/or CDATA node sizes
    * Reformatting of XML within text and/or CDATA nodes
    * Anonymize of element and/or attribute contents
    * Removal of subtrees

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
The project is based on [Maven] and its submodules are available on the central Maven repository.

Snapshot builds are available from the Sonatype OSS [snapshot repository].

# Usage
See submodules for usage instructions and examples:

 * [CXF]
  * [Apache Camel example]
 * [Mule-CXF]
  * [Mule ESB example]
  * [Mule ESB benchmark]
 * [JAX-RS]
  * [Jersey example]
  
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
[snapshot repository]: https://oss.sonatype.org/content/repositories/snapshots/com/greenbird/xml-formatter/xml-formatter-components/
[xml-formatter]:	   https://github.com/greenbird/xml-formatter-core
[Spring Cheat Sheet]:  ./spring.cheat.sheet.md
[Logging-feature]:	    http://cxf.apache.org/docs/features.html
[PrettyPrint]:          ./jax-rs#class-annotation
[JAX-RS]:               ./jax-rs
[CXF]:                  ./cxf
[Jersey example]:       ./jax-rs-example
[Apache Camel example]: ./cxf-example
[Mule-CXF]:             ./mule-cxf
[Mule ESB example]:     ./mule-cxf-example
[Mule ESB benchmark]:   ./cxf-benchmark