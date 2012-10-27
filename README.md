Calculator
==========

A recommended calculation: sin( sqrt(PI) + 1/5 )
Button strokes are: PI, 2nd, sqrt, +, 5, inv, =, sin
You should get the result 0.9204142587521185


===IN SHORT===

The user can only indirectly edit the middle text field. The one above is used for holding the previous number, the one below is for the result. Some operations use both the top and the middle text field, while others use only the middle field. This should make the calculator both intuitive because its similar to how we do operations by hand, and powerful because as shown above my friend can perform complex operations without using any memory/etc. I'd speculate that other calculators aren't capable of this functionality.


===FUNCTIONS===

Functions which take one argument, such as sqr, sqrt, trig functions, etc are immediately applied to the middle text field if and only if the result output is blank, otherwise it's applied to the result output which then becomes the first field so that the next operation can be entered. This makes the calculator very flexible.
	
Functions which take two arguments, such as +, -, *, /, x^y, etc simply change the operator field and allow the user to specify the second number if one isn't already set.


===2nd button===

The "2nd" button toggles and gives access to other operators, constants, and function inverses. Constants include PI, E, the gravitational constant g, and the speed of light c. These constants override the contents of the middle field.


==KEYBOARD SHORTCUTS===

Besides 0-9 and +-*/:
	SHIFT or ALT: activates "2nd" key
	BACKSPACE removes last entered character
	ENTER activate the equals sign button, which calculates the result
	ESCAPE clears all input and resets the calculator
There is a limit on the number of characters you can enter. It will reject further input if this limit is reached.


===ADVANCED FEATURE===
Log of previous operations is shown in a fancy semi-transparent screen on the side of the calculator. Log will fade away after five seconds of inactivity, but can be brought back by hovering the mouse over it. It will not fade away if the mouse is hovering over this log window.


===NOTES===
I've strived to follow the Windows Calculator or be otherwise intuitive for many of the details. For example, if you enter a number when a result is displayed, the fields are cleared to reset, just as the Windows Calculator does. But unlike Windows Calculator, if the calculator performs an invalid operation, it will not crash but will simply output NaN which the user can then continue to attempt to do things with at will.
	