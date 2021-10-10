// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// constant screenPointer = 16384    (@SCREEN)
// constant keyboardPointer = 24576  (@KBD)

//	LOOP
(LOOP)

//	counter = 8191
@8191
D=A
@counter
M=D

//	if (key_pressed)
//		GOTO BLACK
@KBD
D=M
@BLACK
D; JNE

// 	else	
//		WHITE
(WHITE)

//		RAM[screen + counter] = 0
@counter
D=M
@SCREEN
A=D+A
M=0

//		counter = counter-1
@counter
MD=M-1

//		if counter < 0
//			GOTO LOOP
@LOOP
D; JLT

//		else
//			GOTO WHITE
@WHITE
0; JMP

//	BLACK
(BLACK)

//		RAM[screen + counter] = -1
@counter
D=M
@SCREEN
A=D+A
M=-1
//		counter = counter-1
@counter
MD=M-1

//		if counter < 0
//			GOTO LOOP
@LOOP
D; JLT

//		else
//			GOTO BLACK
@BLACK
0; JMP

// 	GOTO LOOP
@LOOP
0; JMP 