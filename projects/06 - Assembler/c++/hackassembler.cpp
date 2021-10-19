#include <stdio.h>
#include <iostream>
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

    // List program arguments
    if (argc != 0) {
        cout << argc << " arguments detected:" << endl;
        for (int i = 0; i < argc; i++) {
            cout << "  " << argv[i] << endl;
        }
    } else {
        cout << "No arguments detected." << endl;
    }

    // TODO - Transfer assembly operation from Java project
    cout << "ERROR - Assembly not implemented." << endl;

    // End program execution
    cout << "Stopping.";
    return 0;
}