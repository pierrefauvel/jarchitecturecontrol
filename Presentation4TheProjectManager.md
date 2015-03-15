**JArchitectureControl** is a framework to help Java Architect control that Java application respect architectures principles and design.

At the beginning of the project, the architect defines applicative **architecture** (layers, naming, allowed dependencies between layers or between design patterns, ...)
This architecture is specified using configuration file.

After than, using continuous integration, each time someone commits a change, all the application is **checked** against the rules and a violation report is issues.

Each violation is reviewed by the architect :
  * case 1 : the rule is good, the code should change. The architect creates a defect with the appropriate priority.
  * case 2 : the rule shows that the architecture should be ammended. The architect changes the specification, tells anybody.

During the whole project :
  * everyone knows the rules
  * the rules are respected
  * the rules evolve to match real life constraints