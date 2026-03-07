# OVERLAY-JVM
JVM Library (e.g., for Java and Kotlin) to support [OpenAPI Overlay](https://github.com/OAI/Overlay-Specification) transformations.

Currently supporting Overlay specs __1.1.0__, and schemas/overlays in either JSON or YAML.

Working for JDK 8 and above. 


## Example Usage

```
String openApi = ...
String overlay = ...
TransformationResult tr = Processor.applyOverlay(openApi, overlay);
tr.transformedSchema; // the result of the transformation
tr.warnings; // list of warning messages, if any 
```

## Use in Maven

```
<dependency>
    <groupId>com.webfuzzing</groupId>
    <artifactId>overlay-jvm</artifactId>
    <version>LATEST</version>
</dependency>
```

See release info for latest version number (to replace `LATEST` placeholder).

## Dependencies

This library has 2 main dependencies:
- [Jackson](https://github.com/FasterXML/jackson-core): to be able to parse JSON/YAML files. 
- [SnackJson](https://github.com/noear/snackjson): for handling RFC 9535 JsonPath, and for DOM manipulation.


## Project Maintenance

This is a small, relatively simple library. 
Apart from bug-fixing and supporting new Overlay specs when they come out, we do not expect much activity here in this project. 

The main driver beyond supporting and maintaining this open-source project is its use in the REST API fuzzer [EvoMaster](https://github.com/WebFuzzing/EvoMaster). 
As long as we support EvoMaster (which has been around since 2016), we will keep supporting this library. 
