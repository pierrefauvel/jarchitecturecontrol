**JArchitectureControl** is a framework intended to help architects control the architecture.

The first part analyses the code of the project (and the code repository (maven)), using reflection and bytecode analysis.

A model of the code is built :
  * **static** : class, method, field, names, types, annotations
  * **semi-dynamic** : method invokes method, method instantiates class, ...

The second part specifies how the code should be :
  * **organized** (packages, layers)
  * **named** (package names, class names, methods names, ...)
  * **typed** (types, annotations)
  * **designed** (design patterns, dependencies, dynamic relationships)

The model is then checked against the specification and a list of violations is issued.