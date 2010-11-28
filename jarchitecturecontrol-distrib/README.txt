#To generate "database" files for the test project petclinic
#WILL ERASE EXISTENT DATABASE (but not history)
#Will create configuration, tagrepository & specification directory

java -jar jarchitecturecontrol-test-petclinic-0.1.jar


#To launch the Swing client to setup configuration, tag repository & specification and run control in order to obtain violations
java -jar jarchitecturecontrol-core-0.1.jar

Clic on Configuration Editor button
(automatically restored from database)
Browse the tree
Right click on a Cfg node (configuration) + Select configuration
Close the Dialog

Clic on TagRepository Editor button
(not automatically restored from database)
Right Clic on TagRepositoryInternal node
Click on restore from file
Browse the tree
Close the Dialog

Click on Specification Editor button
(not automatically restored from database)
Right clic on the root node
Click on restore from file
Browse the Tree
Close the Dialog

Click on Run control
Browse the violations
Close the Dialog

Click on any "Editor" button to change configuration, tags repository or specification
Click on Run control to run a control and see the resulting violations
