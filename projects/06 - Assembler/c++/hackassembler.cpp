#include <stdio.h>
#include <iostream>
#include <string>
using namespace std;

/**
 *  Parse a text file written in the Hack Assembly language into a file
 *    containing 16-bit machine code for the Hack computer.
 *  @author Kieron Gillingham <kierongillingham@gmail.com>
 */

int main(int argc, char *argv[]) {
    // Print details
    cout << "--Hack Assembler--" << endl << "-Kieron Gillingham- -2021-" << endl;

    // Start program execution
    cout << "Starting." << endl;

    // Handle program arguments
    if (argc != 0) {

        // List arguments for debugging
        cout << argc << " arguments detected:" << endl;
        for (int i = 0; i < argc; i++) {
            cout << "  " << argv[i] << endl;
        }

    } else {
        cout << "No arguments detected." << endl;
    }

    // Parse program arguments
    // Handle input file
    string filepath = "file.asm"; // Default input filepath
    // Get new filepath if given as argument
    if (argc > 1) {
        char* arg = argv[1];
        filepath = arg;
    }
    cout << "Assembling " << filepath << endl; // Output filepath to be used

    // TODO - Transfer assembly operation from Java project
    cout << "ERROR - Assembly not implemented." << endl;

    // End program execution
    cout << "Stopping." << endl;
    return 0;
}