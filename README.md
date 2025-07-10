## HOW TO RUN A JADE PROGRAM ON ECLIPSE:
- Import the project in your workspace
- Download Jade library from https://jade.tilab.com/download/jade/license/jade-download/, more precisely the jadeBin package.
- Right-click on the project->Build Path->Configure Build Path
- Libraries->Classpath->Add External JARs
- Inside the downloaded file there will be a jade.jar file, inside jade/lib/. Add that file
- Apply and close
- Right-click on the project->Run as->Run Configuration->Java Application->New Run Configuration.
- Go into Dependencies->Classpath Entries and add the downloaded jar. Apply and close.
- Check if there is a module-info.java file. If it exists, delete it from the project.
- Return into Dependencies->Classpath Entries and click Restore Default Entries.
- Now the Modulepath Entries section shuold be empty, and the Classpath Entries section should contain the project, jade.jar and JRE System Library.
- Apply
- Go into Main->Main.class and type jade.Boot.
- Go into Arguments, and add -gui AgentName:packagename.classname;Agent2Name:packagename.classname as Program Arguments. AgentName, packagename and classname according to the program.
- The AgentName attribute is the name of the agent, and can be whatever you want.
- For example, in this program, I put -gui Agent1:test.FirstAgent;Agent2:test.FirstAgent;Agent3:test.FirstAgent to create 3 agents, named Agent1, Agent2 and Agent3 from the class FirstAgent in the package test.


This manual has been written with the help of ChatGPT. It helped me on the setup of the project, as I was getting different errors, mainly because of the module-info.java file.
If you have any problem running the program, you can search for documentaion at https://jade.tilab.com/documentation/tutorials-guides/ or ask ChatGPT
