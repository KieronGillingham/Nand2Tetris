#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <cctype>
#include <algorithm>
#include <exception>

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

bool strcmpb(const char *_Str1, const char *_Str2) {
    return strcmp(_Str1, _Str2) == 0;
}

int main(int argc, char *argv[]) {
    // Print details
    cout << "--Hack Assembler--" << endl << "-Kieron Gillingham- -2022-" << endl;

    // Handle program arguments
    try {
        if (argc >= 2) {

            // List arguments for debugging
            cout << argc << " arguments detected:" << endl;
            for (int i = 0; i < argc; i++) {
                cout << "  " << argv[i] << endl;
            }

            // Handle arguments
            for (int i = 0; i < argc; i++) {
                if (strcmpb(argv[i], "-h") || strcmpb(argv[i], "--help")) {
                    cout << "Usage: hackassembler [-h | --help] input_file [-o output_file | --output output_file]" << endl;
                    return 0;
                }
            }
        } else {
            throw runtime_error("ERROR - Missing arguments. See 'hackassembler -h' for usage instructions.");
        }

    } catch (exception& e) {
        cerr << e.what() << endl;
        return 1;
    }

    // Parse program arguments
    // Handle input file
    string infilepath = "./file.asm"; // Default input filepath
    // Get new filepath if given as argument
    if (argc > 1) {
        char* arg = argv[1];
        infilepath = arg;
    }
    string outputfilepath = "output.txt";

    // Start program execution
    cout << "Starting." << endl;

    cout << "Assembling " << infilepath << " to " << outputfilepath << endl; // Output filepath to be used

    try {
        // Create streams to read/write file
        ifstream inputFile(infilepath);
        
        // Check file was opened successfully
        if (!inputFile.is_open()) {
            // Input file not found
            throw runtime_error("ERROR - Input file '" + infilepath + "' could not be opened.");
        }

        // File opened successfully
        cout << "File opened." << endl;

        // Create instance of HackAssembler to perform translation
        HackAssembler assembler = HackAssembler();
        string fileLine;
        ofstream outFile;
        outFile.open(outputfilepath);
        if (!outFile) {
            throw runtime_error("ERROR - Output file '" + outputfilepath + "' could not be created.");
        }
        while (getline(inputFile, fileLine)) {
            // Translate current line and output result to console
            fileLine = assembler.translate(fileLine);
            outFile << fileLine << endl; 
        }
        outFile.close();
        cout << "Translation complete." << endl;

    } catch (exception& e) {
        cerr << e.what() << endl;
        return 1;
    }

    // End program execution
    cout << "Stopping." << endl;
    return 0;
}