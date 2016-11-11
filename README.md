AaaController
===============

Software for controlling LZ1AQ's Active Antenna Amplifier kit
For more info see http://www.active-antenna.eu/

The program is written in Java and supports the following operating systems:
linux, windows, macos


Installation:
--------------

1) Make sure you have installed Java Runtime Environment (JRE) version 1.7 or later.

2) Optional: From the button "branch:master" select "tags" and select the newest available version

3) Download the .zip file from the `/distribution` directory by **left-pressing the mouse key** and then selecting **RAW button**. 

4) Extract the archive in a directory of your choice.

4) Start the program by using the **startup.bat** (for Windows) or the **starup.sh** (for Linux)

**In case of problems**: 
[Linux] If you have started the program but you don't see the comm port in the "Settings" dialog, 
probably you don't have the permission to access the commport file (e.g. /dev/ttyS0)
* You need to add your username to the `dialout` group by writing: `sudo gpasswd --add yourusername dialout`
* Then either login and logout or write `exec su – $USER`


Development - IDE Installation:
-----------------

First clone or download the project from github.com/potty-dzmeia/AaaController

1) Netbeans [recommended]
* Go to `File->Open Project` and select the directory where you have downloaded the project
* Now you are ready to run and debug the project

2) Eclipse
* From command line in root directory of the project write `mvn eclipse:eclipse`
  * This creates all the needed files so that Eclipse recognizes the folder as a project
* From Eclipse select `File->Import->General->Existing Project into Workspace` and point to the place where you have downloaded the project
* Right click on the project and select `Run As->Run Configurations...` Double-click on `Java Application`
  * In the `Main` tab specify the `Main Class:` to be  `App` 
  * Press on `Apply` and then `Run`
* Note that every time you make changes in the source code you will need to: 
  * Write again in the command line `mvn clean install` in order to compile your changes 
  * Then from Eclipse right-click on the project and select `Run As(or Debug As)->Java Application`
