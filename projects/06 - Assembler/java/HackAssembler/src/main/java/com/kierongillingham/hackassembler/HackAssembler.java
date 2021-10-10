package com.kierongillingham.hackassembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Kieron Gillingham <kierongillingham@gmail.com>
 *    Parse a text file written in the Hack Assembly language into a file
 *    containing 16-bit machine code for the Hack computer.
 */
public class HackAssembler
{
    final static Logger LOG = LogManager.getLogger(HackAssembler.class);
    
    public static void main(String[] args) throws IOException
    {
        Path asmFilePath = Paths.get(args[0]); //Filepath to the file to be translated
        if (!asmFilePath.isAbsolute())
        {
            LOG.debug("Input file is not absolute.");
        }
        
        boolean verboseMode = false; 
        boolean outputMode = false;
        
        for (int i = 1; i < args.length; i++)
        {
            String flag = args[i];
            if (flag.equals("-v") || flag.equals("--verbose"))
            {
                verboseMode = true;
                LOG.info("Verbose Mode set");
            }
            
            if (flag.equals("-o") || flag.equals("--output-file"))
            {
                outputMode = true;
                LOG.info("File output set");
            }
        }
        
        String asmFileName = FilenameUtils.getBaseName(asmFilePath.toString());
        Path outputFilePath = asmFilePath.getParent().resolve(asmFileName + ".hack");
        
        LOG.debug("Input file: " + asmFilePath);
        
        if (outputMode) {
            outputFilePath.toFile().createNewFile();
            LOG.debug("Output file: " + outputFilePath);
        }
        
        HashMap<String, String> cComp = new HashMap<>();
        cComp.put("0"  , "0101010");
        cComp.put("1"  , "0111111");
        cComp.put("-1" , "0111010");
        cComp.put("D"  , "0001100");
        cComp.put("A"  , "0110000");
        cComp.put("!D" , "0001101");
        cComp.put("!A" , "0110001");
        cComp.put("-D" , "0001111");
        cComp.put("-A" , "0110011");
        cComp.put("D+1", "0011111");
        cComp.put("A+1", "0110111");
        cComp.put("D-1", "0001110");
        cComp.put("A-1", "0110010");
        cComp.put("D+A", "0000010");
        cComp.put("D-A", "0010011");
        cComp.put("A-D", "0000111");
        cComp.put("D&A", "0000000");
        cComp.put("D|A", "0010101");
        cComp.put("M"  , "1110000");
        cComp.put("!M" , "1110001");
        cComp.put("-M" , "1110011");
        cComp.put("M+1", "1110111");
        cComp.put("M-1", "1110010");
        cComp.put("D+M", "1000010");
        cComp.put("D-M", "1010011");
        cComp.put("M-D", "1000111");
        cComp.put("D&M", "1000000");
        cComp.put("D|M", "1010101");
        
        HashMap<String, String> cDest = new HashMap<>();
        cDest.put(null, "000");
        cDest.put("M"   , "001");
        cDest.put("D"   , "010");
        cDest.put("MD"  , "011");
        cDest.put("A"   , "100");
        cDest.put("AM"  , "101");
        cDest.put("AD"  , "110");
        cDest.put("AMD" , "111");
        
        HashMap<String, String> cJump = new HashMap<>();
        cJump.put(null, "000");
        cJump.put("JGT" , "001");
        cJump.put("JEQ" , "010");
        cJump.put("JGE" , "011");
        cJump.put("JLT" , "100");
        cJump.put("JNE" , "101");
        cJump.put("JLE" , "110");
        cJump.put("JMP" , "111");
        
        HashMap<String, Integer> symbols = new HashMap<>();
        symbols.put("R0", 0);
        symbols.put("R1", 1);
        symbols.put("R2", 2);
        symbols.put("R3", 3);
        symbols.put("R4", 4);
        symbols.put("R5", 5);
        symbols.put("R6", 6);
        symbols.put("R7", 7);
        symbols.put("R8", 8);
        symbols.put("R9", 9);
        symbols.put("R10", 10);
        symbols.put("R11", 11);
        symbols.put("R12", 12);
        symbols.put("R13", 13);
        symbols.put("R14", 14);
        symbols.put("R15", 15);
        symbols.put("SCREEN", 16384);
        symbols.put("KBD", 24576);
        symbols.put("SP", 0);
        symbols.put("LCL", 1);
        symbols.put("ARG", 2);
        symbols.put("THIS", 3);
        symbols.put("THAT", 4);
        int nextEmptyVar = 16;
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        
        try 
        {
            br = new BufferedReader(new FileReader(asmFilePath.toString()));      
            if (outputMode) {
                bw = new BufferedWriter(new FileWriter(outputFilePath.toString()));
            }
            
            br.mark(1048576);
            int runningLineNumber = 0;
            
            // First iteration finds and stores user defined symbols
            for(String line; (line = br.readLine()) != null;)
            {
                // Erase comments and remove any trailing whitespace
                line = line.replaceAll("//.*", "");
                line = line.trim();
                
                // Count actual lines
                //   Note that runningLineNumber is not incremented here
                if (line.isEmpty())
                {
                    continue;
                }
                else if (line.matches("\\((.*?)\\)")) // Regex matches any string surrounded by brackets ( )
                {
                    if (verboseMode) {
                        LOG.info(line + " symbol identified on line " + runningLineNumber);
                    }
                    symbols.put(line.replaceAll("[\\(\\)]", ""), runningLineNumber); // Regex matches '(' and ')' characters
                }
                else {
                    runningLineNumber++;
                }
            }
            
            
            
            br.reset();
            runningLineNumber = 0;
            
            for(String line; (line = br.readLine()) != null;)
            {
                // Remove any trailing whitespace.
                line = line.replaceAll("//.*", "");
                line = line.trim();
                // If first character is " ", "/", or "(", it doesn't count as a line.
                
                String thisLine = "";

                // Skip whitespace and comments
                //   Note that realLineNumber is not incremented here
                if (line.equals(""))
                {
                    if(verboseMode)
                    {
                        LOG.info("n/a: " + line);
                    }
                    continue;
                }
                else if (line.startsWith("("))
                {
                    if(verboseMode)
                    {
                        LOG.info("  " + line);
                    }
                    continue;
                }
                
                if(verboseMode)
                    {
                        LOG.info(runningLineNumber + ": " + line);
                    }
                
                if (line.contains("@")) {
                    line = line.replace("@", "");
                    int address = 0;
                    try {
                        address = Integer.parseInt(line);
                    } catch (NumberFormatException ex) {
                        if (symbols.containsKey(line)) {
                            address = symbols.get(line);
                        } else {
                            address = nextEmptyVar;
                            symbols.put(line, nextEmptyVar);
                            nextEmptyVar++;
                        }
                    }
                    String binAddress = Integer.toBinaryString(address);
                    String binAddress15 = String.format("%15s", binAddress).replace(" ", "0");
                    thisLine = "0" + binAddress15;
                } else {
                    String dest = null, comp = null, jump = null, command = line;
                    String[] commandSplit = null;
                    if (command.contains(";")) {
                        
                        commandSplit = command.split(";");
                        //LOG.debug("Jump instruction found: " + Arrays.toString(commandSplit));
                        command = commandSplit[0];
                        jump = commandSplit[1];
                    }
                    
                    if (command.contains("=")) {
                        commandSplit = command.split("=");
                        //LOG.debug("Dest and Comp found: " + Arrays.toString(commandSplit));
                        dest = commandSplit[0];
                        comp = commandSplit[1];
                    } else {
                        if (commandSplit != null) {
                            comp = commandSplit[0];
                            //LOG.debug("Comp found: " + comp);
                        }
                    }
                    
                    thisLine = "111" + cComp.get(comp) + cDest.get(dest) + cJump.get(jump); // Unsure, check specs
                }
                
                
                
                
                
                
                // Print output to log
                LOG.info(thisLine);
                
                // Output to file
                if (bw != null) {
                    bw.write(thisLine + "\n");
                }
                
                // Increment line counter for next loop
                runningLineNumber++;
               
            }
            
            if (verboseMode) {
                LOG.info(symbols);
            }
            
        }
        
        catch(Exception ex)
        {
            LOG.error(ex);
        }
        
        finally
        {
            if (br != null) {
                br.close();
            }
            
            if (bw != null) {
                bw.close();
            }
        }
    }
}