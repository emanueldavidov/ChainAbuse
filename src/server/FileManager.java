package server;

import java.io.File;
import java.util.ArrayList;

import common.AddressReport;

/**
 * FileManager handles file-related operations such as reading input files,
 * writing logs, and saving reports. Implements the Singleton pattern to ensure
 * a single instance throughout the application. It also acts as an EventSubscriber
 * to handle specific events.
 */
public class FileManager implements EventSubscriber {

    // Singleton instance
    private static FileManager instance = null;

    // Event manager for notifying subscribers
    private final EventDispatcher eventManager;

    // Managers for specific file operations
    private final ExcelFileManager excelFileManager;
    private final TextFileManager textFileManager;

    /**
     * Private constructor for Singleton implementation.
     */
    private FileManager() {
        // Initialize file operation managers
        excelFileManager = new ExcelFileManager();
        textFileManager = new TextFileManager("log.txt");

        // Initialize the EventDispatcher for this FileManager
        eventManager = new EventDispatcher("Result Upload", "Result Save", "Error");
    }

    
    //Provides the Singleton instance of FileManager.
  
    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    
    // Returns the EventDispatcher associated with this FileManager.
   
    public EventDispatcher getEventDispatcher() {
        return eventManager;
    }

    /**
     * Handles events triggered by the EventDispatcher.
     *
     * @param eventType The type of event.
     * @param obj       The event data associated with the event.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(String eventType, Object obj) {
        try {
            if (eventType.equals("Save Report")) {
                handleSaveReportEvent((ArrayList<Object>) obj);
            } else if (eventType.equals("Upload File")) {
                handleUploadFileEvent((File) obj);
            } else if (eventType.equals("Log")) {
                handleLogEvent((String) obj);
            } else {
                throw new IllegalArgumentException("Unhandled event type: " + eventType);
            }
        } catch (Exception e) {
            eventManager.notify("Error", eventType);
        }
    }

    /**
     * Handles the "Save Report" event by writing data to an Excel file.
     *
     * @param data The data to save, including the directory path and the report entries.
     * @throws Exception If the save operation fails.
     */
    private void handleSaveReportEvent(ArrayList<Object> data) throws Exception {
        ArrayList<AddressReport> reportEntries = (ArrayList<AddressReport>) data.get(1);
        String directoryPath = (String) data.get(0);

        boolean result = excelFileManager.saveToExcel(directoryPath, reportEntries);
        if (result) {
            eventManager.notify("Result Save", true);
        } else {
            throw new Exception("Failed to save report.");
        }
    }

    /**
     * Handles the "Upload File" event by reading an input file.
     *
     * @param file The file to read.
     * @throws Exception If the file read operation fails.
     */
    private void handleUploadFileEvent(File file) throws Exception {
        ArrayList<String> data = textFileManager.readInputFile(file);
        if (data != null) {
            eventManager.notify("Result Upload", data);
        } else {
            throw new Exception("Failed to read input file.");
        }
    }

    
     // Handles the "Log" event by writing to a log file.
  
    private void handleLogEvent(String logMessage) throws Exception {
        if (!textFileManager.writeToLogFile(logMessage)) {
            throw new Exception("Failed to write to log file.");
        }
    }
}
