# Java Watchface test
Generates a watchface using Java JFrame for test purposes.

This is a Java implementation of a watchface for general tests. 
It uses JFrames to build the GUI. Despite the little size of the file, it was noted that it presents several challenges for a developer´s first approach.
The first problem encountered was how to solve a shift in the digital clock refresh (at each second) when we remove the character ":" of a string of type "88:88:88" when the seconds' final number is an odd one.
The solution encountered was to get each character of the string and draw it separately, suppressing the ":" character with a blank one.

The second problem encountered was how to draw a line so that we could have control of its thickness? 
The solution adopted was to draw a polygon instead of a line, so that it was possible to control the polygon form by varying its coordinates accordingly.
It was implemented a method to rotate de polygon to the new position. 
However, this solution is not totally convenient because a flicker was perceived 
 during the refresh time. One way to solve this problem could be trying to find a way to refresh only parts of the screen that are doing animation and not to refresh all the screen.

The approximated time to do this project was  2 days, using approximately 12 hours, considering that it happened to be a high demand for personal issues this weekend.


