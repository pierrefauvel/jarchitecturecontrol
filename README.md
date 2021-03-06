# jarchitecturecontrol
Automatically exported from code.google.com/p/jarchitecturecontrol

The goal of *JArchitectureControl* is to provide a DSL for architects to specify and control the respect of architecture rules by the code.

The framework analyses code (and maven repository dependencies) :
 * reflection meta data (static analysis) : types, modifiers, annotations
 * byte code meta data (semi-dynamic analysis) : methods invoked, instantiated classes, ...)

The framework visits the code in regards to the specification provided by the architect (naming, properties, relations ships), tags code elements, reports rules violations.

You can find the Need-Solution analysis that led to this project at [http://softreves.wordpress.com/2010/02/10/n-s-check-principes-darchitecture/]

The following progress have been achieved :
||analysis of code||ok (maven only)||
||reflection based analysis||ok||
||bytecode based analysis||ok||
||specification||ok (api, xml and gui)||
||control||ok (gui only)||
||reporting||ok (both xml and gui)||
||testing||ok: detailed unit test cases for the tricky parts (mainly the bytecode analysis and the control)||
||used in production||ok (used twice for real audits)||

Future (short-term) directions may be :
 * extended and automated testing of the bytecode analysis
 * use with eclipse projects instead of maven projects
 * integration as a maven and sonar plugin, generating html reports, uploading configuration files to the server

==[Presentation4TheContributor We need contributors (commiters) !]==

