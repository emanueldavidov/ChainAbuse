package client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import common.AddressDetail;
import common.AddressReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.EventSubscriber;
import server.EventDispatcher;

/**
 * The GuiController class manages the GUI interactions, 
 * handles user actions, and integrates with the event-driven system.
 */
public class GuiController implements EventSubscriber {

    @FXML
    private TextField tf_newAddress;
    @FXML
    private TextArea ta_log;

    @FXML
    private Button btn_addToList;
    @FXML
    private Button btn_scan;
    @FXML
    private Button btn_saveResults;
    @FXML
    private Button btn_clearTable;

    @FXML
    private VBox vb_uploadFile;

    @FXML
    private TableView<AddressDetail> tv_address;
    @FXML
    private TableView<AddressReport> tv_results;

    private EventDispatcher eventDispatcher;

    private ObservableList<AddressDetail> addressTable = FXCollections.observableArrayList();
    private ObservableList<AddressReport> reportResult = FXCollections.observableArrayList();

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final FileChooser fileChooser = new FileChooser();
    private Stage primaryStage;
    private AddressDetail editEntry;
    private boolean editFlag;

    /**
     * Initializes the GUI controller with the provided stage.
     */
    public void initGuiController(Stage primaryStage_) {
        primaryStage = primaryStage_;
        editEntry = null;
        editFlag = false;
        initAddressTableView();
        initResultTableView();
        initDirectoryChooser();
        initFileChooser();
    }

    private void initAddressTableView() {
        TableColumn<AddressDetail, Void> singleColumn = new TableColumn<>("Address");
        singleColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(5); // Reduced spacing
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    AddressDetail entry = getTableView().getItems().get(getIndex());
                    Label addressLabel = new Label(entry.getAddress());
                    addressLabel.setMaxWidth(Double.MAX_VALUE);
                    addressLabel.setStyle("-fx-text-overrun: ellipsis;"); // Ellipsis for long text
                    HBox.setHgrow(addressLabel, Priority.ALWAYS);

                    // Edit button
                    Button editButton = new Button();
                    ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("edit.png")));
                    editIcon.setFitWidth(16); // Smaller icon
                    editIcon.setFitHeight(16);
                    editButton.setGraphic(editIcon);
                    editButton.setStyle("-fx-background-color: transparent; -fx-padding: 2px;"); // Smaller padding
                    editButton.setOnAction(e -> {
                        editEntry = entry;
                        tf_newAddress.setText(entry.getAddress());
                        btn_addToList.setText("Update");
                        editFlag = true;
                    });

                    // Remove button
                    Button removeButton = new Button();
                    ImageView removeIcon = new ImageView(new Image(getClass().getResourceAsStream("trash.png")));
                    removeIcon.setFitWidth(16); // Smaller icon
                    removeIcon.setFitHeight(16);
                    removeButton.setGraphic(removeIcon);
                    removeButton.setStyle("-fx-background-color: transparent; -fx-padding: 2px;"); // Smaller padding
                    removeButton.setOnAction(e -> {
                        addressTable.remove(entry);
                        updateAddressTableView();
                    });

                    hBox.getChildren().addAll(addressLabel, editButton, removeButton);
                    setGraphic(hBox);
                }
            }
        });

        tv_address.getColumns().clear();
        tv_address.getColumns().add(singleColumn);
        tv_address.setItems(addressTable);

        singleColumn.prefWidthProperty().bind(tv_address.widthProperty().subtract(2)); // Bind column width

        // Add alternating row colors
        tv_address.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(AddressDetail item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle(""); // Reset style
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #E9EBF5;");
                    } else {
                        setStyle("-fx-background-color: #CFD4EA;");
                    }
                }
            }
        });

        // Prevent horizontal scrolling
        tv_address.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initResultTableView() {
        TableColumn<AddressReport, String> column1 = new TableColumn<>("Address");
        column1.setCellValueFactory(new PropertyValueFactory<>("address"));
        tv_results.getColumns().add(column1);

        TableColumn<AddressReport, String> column2 = new TableColumn<>("Report Count");
        column2.setCellValueFactory(new PropertyValueFactory<>("reportCount"));
        tv_results.getColumns().add(column2);

        TableColumn<AddressReport, ImageView> column3 = new TableColumn<>("Status");
        column3.setCellValueFactory(new PropertyValueFactory<>("img"));
        tv_results.getColumns().add(column3);

        // Create the "Link" column to work with Hyperlink
        TableColumn<AddressReport, Hyperlink> column4 = new TableColumn<>("Link");
        column4.setCellValueFactory(new PropertyValueFactory<>("link"));
        column4.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Hyperlink link, boolean empty) {
                super.updateItem(link, empty);
                if (empty || link == null) {
                    setGraphic(null);
                } else {
                    setGraphic(link); // Directly use the Hyperlink
                    link.setOnAction(event -> {
                        try {
                            // Open the link in the default browser
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(link.getText()));
                        } catch (Exception e) {
                            e.printStackTrace(); // Log any errors
                        }
                    });
                }
            }
        });
        tv_results.getColumns().add(column4);

        column1.setPrefWidth(270);
        column2.setPrefWidth(80);
        column3.setPrefWidth(50);
        column4.setPrefWidth(155);

        // Add alternating row colors
        tv_results.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(AddressReport item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #E9EBF5;"); // Light blue for even rows
                    } else {
                        setStyle("-fx-background-color: #CFD4EA;"); // Dark blue for odd rows
                    }
                }
            }
        });
    }

    
    
    private void initDirectoryChooser() {
        directoryChooser.setTitle("Choose Output Folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    }

    private void initFileChooser() {
        fileChooser.setTitle("Open Input File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    }

    private void updateAddressTableView() {
        tf_newAddress.clear();
        editEntry = null;
        editFlag = false;
        btn_addToList.setText("Add to list");
        tv_address.setItems(addressTable);
    }

    private void updateResultTableView() {
        tv_results.setItems(reportResult);
    }

    private void updateLogTextArea(String message) {
        String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime());
        String logEntry = timeStamp + " " + message + "\n";

        // Check if message contains "failed" or "successfully" and set the color accordingly
        if (message.toLowerCase().contains("failed")) {
            ta_log.setStyle("-fx-text-fill: red; -fx-border-color: black;");  // Red color for failed
        } else if (message.toLowerCase().contains("successfully")) {
            ta_log.setStyle("-fx-text-fill: green; -fx-border-color: black;");  // Green color for success
        } else {
            ta_log.setStyle("-fx-text-fill: black; -fx-border-color: black;");  // Default black color
        }

        // Append the log entry
        ta_log.appendText(logEntry);
        
        // Optionally notify
        eventDispatcher.notify("Log", logEntry);
    }


    @FXML
    void addToList(ActionEvent event) {
        String newAddress = tf_newAddress.getText();
        if (!newAddress.isEmpty()) {
            if (editFlag && editEntry != null) {
                ObservableList<AddressDetail> tempAddressTable = FXCollections.observableArrayList();
                AddressDetail updatedEntry = new AddressDetail(newAddress);
                for (AddressDetail entry : addressTable) {
                    if (entry.equals(editEntry)) {
                        tempAddressTable.add(updatedEntry);
                    } else {
                        tempAddressTable.add(entry);
                    }
                }
                addressTable.clear();
                addressTable = tempAddressTable;
            } else {
                AddressDetail newEntry = new AddressDetail(newAddress);
                addressTable.add(newEntry);
            }
            updateAddressTableView();
        } else {
            editEntry = null;
            editFlag = false;
            btn_addToList.setText("Add to list");
        }
    }


    @FXML
    void clearTable(ActionEvent event) {
        addressTable.clear();
        updateAddressTableView();
    }


    @FXML
    void uploadFile(MouseEvent event) {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            eventDispatcher.notify("Upload File", selectedFile);
        }
    }

    @FXML
    void scanAddresses(ActionEvent event) {
        eventDispatcher.notify("Scan Addresses", new ArrayList<>(addressTable));
    }

    @FXML
    void saveResults(ActionEvent event) {
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            ArrayList<Object> saveData = new ArrayList<>();
            saveData.add(selectedDirectory.getPath());
            saveData.add(new ArrayList<>(reportResult));
            eventDispatcher.notify("Save Report", saveData);
        }
    }

    @Override
    public void onEvent(String eventType, Object eventData) {
        switch (eventType) {
            case "Scan Results":
                handleScanResults(eventData);
                break;
            case "Error":
            	handleError(eventData);
            	break;
            case "Result Upload":
                handleResultUpload(eventData);
                break;
            case "Result Save":
                handleResultSave(eventData);
                break;
        }
    }

    private void handleScanResults(Object eventData) {
        try {
            if (eventData instanceof RuntimeException) {
                // Ignore errors here, as they will be handled by "Error"
                return;
            }

            ArrayList<AddressReport> entries = (ArrayList<AddressReport>) eventData;
            reportResult.clear();
            for (AddressReport entry : entries) {
                int reportCount = Integer.parseInt(entry.getReportCount()); // Ensure conversion to int
                entry.setImg(new ImageView(new Image(getClass().getResourceAsStream(
                    reportCount > 0 ? "danger.png" : "ok.png"
                ))));
                reportResult.add(entry);
            }
            updateResultTableView();
            updateLogTextArea("Scan completed successfully.");
        } catch (NumberFormatException e) {
            updateLogTextArea("Scan failed: Report count is not a valid number.");
        } catch (Exception e) {
            updateLogTextArea("Scan failed due to an exception.");
        }
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void handleResultUpload(Object eventData) {
        try {
            ArrayList<String> addresses = (ArrayList<String>) eventData;
            addressTable.clear();
            for (String address : addresses) {
                addressTable.add(new AddressDetail(address));
            }
            updateAddressTableView();
        } catch (Exception e) {
            System.err.println("Error occurred during result upload.");
        }
    }

    private void handleResultSave(Object eventData) {
        boolean isSaved = (Boolean) eventData;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Results");
        alert.setHeaderText(isSaved ? "Report saved successfully." : "Report save failed.");
        alert.showAndWait();
    }

    private void handleError(Object eventData) {
        String errorMessage = eventData instanceof String
                ? (String) eventData
                : "An unknown error occurred.";

        showErrorAlert("Error", errorMessage);
        updateLogTextArea("Scan failed: " + errorMessage); // Write error to the log area
    }


    public void setEventDispatcher(EventDispatcher dispatcher) {
        this.eventDispatcher = dispatcher;
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
}