#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <cctype>
#include <algorithm>
using namespace std;

/**
 *  Parse a text file written in the Hack Assembly language into a file
 *    containing 16-bit machine code for the Hack computer.
 *  @author Kieron Gillingham <kierongillingham@gmail.com>
 */

class HackAssembler {
    public:
        /**
         *   Perform operation on an input string. 
         *   @param input The string to be translated.
         *   @returns A lowercase version of 'input' using 'std::tolower()'.
         */
        string translate(string input) {
            // Transform the input string by calling tolower() on each char.
            transform(input.begin(), input.end(), input.begin(), [](unsigned char c){ return tolower(c); });
            return input;
        }

};

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
    string filepath = "./file.asm"; // Default input filepath
    // Get new filepath if given as argument
    if (argc > 1) {
        char* arg = argv[1];
        filepath = arg;
    }
    cout << "Assembling " << filepath << endl; // Output filepath to be used

    
    try {
        // Create ifstream to read file
        ifstream inputFile(filepath);

        // Check file was opened successfully
        if (!inputFile.is_open()) {
            // Input file not found
            cout << "ERROR - Input file '" << filepath << "' could not be opened." << endl;
            return 0;
        }

        // File opened successfully
        cout << "File opened." << endl;

        // Create instance of HackAssembler to perform translation
        HackAssembler assembler = HackAssembler();
        string fileLine;
        while (getline(inputFile, fileLine)) {
            // Translate current line and output result to console
            fileLine = assembler.translate(fileLine);
            cout << "  " << fileLine << endl; 
        }

    } catch (int e) {
        cout << "Error: " << e << endl; 
    }

    // End program execution
    cout << "Stopping." << endl;
    return 0;
}