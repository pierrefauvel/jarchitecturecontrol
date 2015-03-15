### Vision ###
The goal is to provide architects with a tool to :
  * analyse code
  * specify architecture rules
  * control
  * generate reports

The tool will be integrated in the projects build tool.
### Constraints ###
The project should produce a framework/component easy to integrate by an architect on a new project :
  * should scale up (application code can be quite big). Limit memory footprint. Should run fast.
  * plain java. Not too many dependencies (for the time being, only bcel)
  * as much code repository agnostic as possible (for the time being, maven, but eclipse should be handled soon)
  * no assumptions on the way it will be integrated to a build platform
  * easy to specify. A xml format for specification should be provided, but also an API to build specifications with extra rules
  * focused on the target

This is supposed to be a simple framework, used by architects ("power" developers).
No integrated GUI.

### Code style ###
The fewer code, the better.

### Testing ###
Some aspects will be exhaustively tested using unit test (mostly code analysis)
Some will be tested using a sample application.

### Contact ###
You can contact us at pierre (dot) fauvel (at) gmail (dot) com