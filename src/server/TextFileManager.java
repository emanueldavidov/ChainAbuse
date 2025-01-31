package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles operations for reading and writing to text files, including log management.
 */
public class TextFileManager {

    // Path to the log file
    private String logPath;

    /**
     * Constructor to initialize the log file manager with a specified log file name.
     * Ensures the file is created if it doesn't exist.
     *
     * @param logFileName Name of the log file.
     */
    public TextFileManager(String logFileName) {
        logPath = "./txt/" + logFileName;
        try {
            File file = new File(logPath);
            if (file.createNewFile()) {
                System.out.println("File created: " + logFileName);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while opening: " + logPath + ".");
        }
    }

    /**
     * Reads lines from a given input file and returns them as an ArrayList of strings.
     *
     * @param file The file to read from.
     * @return A list of strings representing the file's content, or null if an error occurs.
     */
    public ArrayList<String> readInputFile(File file) {
        ArrayList<String> stringList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                stringList.add(scanner.nextLine());
            }
            scanner.close();
            System.out.println("readInputFile success.");
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred while reading: " + file.getName() + ".");
            return null;
        }
        return stringList;
    }

  
    // Appends a log message to the log file.
  
    public boolean writeToLogFile(String logMessage) throws IOException {
        try {
            FileWriter logWriter = new FileWriter(logPath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(logWriter);
            bufferedWriter.write(logMessage);
            bufferedWriter.newLine(); // Add a newline for better formatting
            bufferedWriter.close();
            logWriter.close();
            System.out.println("writeToLogFile success.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to log.");
            return false;
        }
        return true;
    }
}
