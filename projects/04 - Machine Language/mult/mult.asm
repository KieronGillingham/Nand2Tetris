// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

// R2 = 0
// if R0 = 0 || R1 = 0:
//	JUMP END

// counter = R0

// LOOP:
// 	R2 = R1 + R2
// 	counter = counter - 1
// 	if counter != 0:
//		JUMP LOOP

// END

// TODO: Needs to reset R0 and R1 to starting values - Implement counter for loop instead of decrementing R0


// 	R2 = 0
@R2
M=0 

// 	if R0 == 0: JUMP END
@R0
D=M
@END
D; JEQ

// 	if R1 == 0: JUMP END
@R1
D=M
@END
D; JEQ

//	counter = R0
@R0
D=M
@counter
M=D

// 	LOOP:
(LOOP)

// 	R2 = R1 + R2
@R1
D=M
@R2
M=D+M

// 	counter = counter - 1
// 	if counter != 0: JUMP LOOP

@counter
M=M-1
D=M
@LOOP
D; JNE

(END)
@END
0; JMP