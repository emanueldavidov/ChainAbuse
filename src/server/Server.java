package server;

import client.GuiController;

/**
 * Manages the server-side components and event dispatching.
 */
public class Server {

    private ChainAbuse chainAbuse;
    private FileManager fileManager;
    private GuiController guiController;

    public Server(GuiController guiController) {
        this.guiController = guiController;

        // Initialize the FileManager and ChainAbuse components
        fileManager = FileManager.getInstance();
        chainAbuse = new ChainAbuse("api_json");

        // Set up the EventDispatcher for the GUI
        EventDispatcher guiEventDispatcher = new EventDispatcher(
                "Scan Addresses", "Upload File", "Save Report", "Log"
        );
        guiController.setEventDispatcher(guiEventDispatcher);

        // Subscribe FileManager's events to the GUIController
        setupFileManagerEvents(guiController);

        // Subscribe ChainAbuse's events to the GUIController
        setupChainAbuseEvents(guiController);

        // Subscribe GUI events to their respective handlers
        setupGuiEvents(guiEventDispatcher);
    }

    /**
     * Sets up event subscriptions for FileManager.
     *
     * @param guiController The GUI controller to be notified.
     */
    private void setupFileManagerEvents(GuiController guiController) {
        EventDispatcher fileManagerEventDispatcher = fileManager.getEventDispatcher();
        fileManagerEventDispatcher.subscribe("Result Upload", guiController);
        fileManagerEventDispatcher.subscribe("Result Save", guiController);
        fileManagerEventDispatcher.subscribe("Error", guiController);
    }


     // Sets up event subscriptions for ChainAbuse.
   
    private void setupChainAbuseEvents(GuiController guiController) {
        chainAbuse.event_manager.subscribe("Scan Results", guiController);
        chainAbuse.event_manager.subscribe("Error", guiController);
    }

    
     //Subscribes GUI events to their respective handlers.

    private void setupGuiEvents(EventDispatcher guiEventDispatcher) {
        guiEventDispatcher.subscribe("Scan Addresses", chainAbuse);
        guiEventDispatcher.subscribe("Upload File", fileManager);
        guiEventDispatcher.subscribe("Save Report", fileManager);
        guiEventDispatcher.subscribe("Log", fileManager);
    }
}
