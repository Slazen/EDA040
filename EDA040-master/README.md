## EDA040
Concurrent and Real-Time Programming at Lund Faculty of Engineering
Credits: 6 hp
Study period: HT 2016

Course responsibles:
Klas Nilsson
Mathias Haage

Formal course description:
http://kurser.lth.se/kursplaner/arets_eng/EDA040.html

# Lab and exercise information
The problems and instructions for each exercise/lab can be found at the end of
the course booklet, available as pdf in the material and reading section
(http://cs.lth.se/eda040/material-and-reading/). At the very end of that document
there are even the solutions to the exercises (but not to the labs; you should
have your well-working solution), to be checked after you solved it.

# Installation
Create a workspace in Eclipse, choose "file->import", pick "general/existing
projects into workspace", select archive file and browse to your zip, import all
projects. Alternatively import directly into eclipse via File->Import->Git.

Coding/design issue: The workspace from 2015 has AlarmClock as a Thread
subclass, as a simple way of having the handout code runnable. If this
(AlarmClock being a Thread object) is what you really want you can keep the code
as it is. However, consider another design with AlarmClock corresponding to main
(as it is in this simulator inside an applet). Then you remove the inheritance
from Thread, and you add a start method that simply starts whatever Thread
objects you created in the main program (i.e. in the AlarmClock constructor).

A new zip with an eclipse HT16 workspace for lab 1 is available. However, there
are changes that should have been reflected in the course material, i.e., for
exercise 1 and 2. You can either:

* Stick to the above HT15 workspace and the instructions already distributed,
  and ignore HT16 workspace.
* Use the above HT15 workspace as it agrees with the exercises and documentation,
  but download and look into the HT16 workspace in case there are some
  useful/interesting hints/details.
* Only use the new HT16 workspace (import as for HT15) and change/write your
  code accordingly (as should be more or less clear from the sources).

In particular the following has been changed in the HT 16 workspace:

* The AlarmClock class is not a thread, the start method is changed, the
  semaphore from ClockInput has a new name, a terminate method is added for the
  optional task, changes in the ClockOutput is  reflected, and the example
  Runnable code more completely demonstrates the input and output features.
* The ClockGUI no longer periodically (in a system dependent manner) signals the
  semaphore when buttons are held down.
* The ClockOutput has some methods for console printout, and the beep is added
  as AWT bell sound.

There is still (to be fixed in the next version) a race condition when buttons
are pushed very frequently on a loaded computer, so your programs do not need to
be robust with respect to that specific situation.
