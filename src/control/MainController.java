package control;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.Main;
import model.Database;
import structures.Node;
import model.Person;
import model.SearchMethod;
import java.io.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private MenuItem loadPrevITEM;

    @FXML
    private MenuItem saveITEM;

    @FXML
    private MenuItem saveExitITEM;

    @FXML
    private MenuItem deleteLogsITEM;

    @FXML
    private MenuItem deletePrevLogsITEM;

    @FXML
    private MenuItem generateITEM;

    @FXML
    private MenuItem addITEM;

    @FXML
    private TextField searchBar;

    @FXML
    private ComboBox<String> searchCBX;

    @FXML
    private ListView<Person> emergentList;

    @FXML
    private Button getResultsBTN;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ImageView verificationImage;

    private HashMap<String, SearchMethod> searchMethodHashMap;

    private ArrayList<Person> coincidentRecords;

    private Image checkImage;

    private Image crossImage;

    public static final int MAX_SHOWN_COINCIDENCES = 100;

    public static final int MIN_ALLOWED_COINCIDENCES_RESULTS = 20;

    public static boolean loadedData;

    public MainController(){
        searchMethodHashMap = new HashMap<>();
        coincidentRecords = new ArrayList<>();
        checkImage = new Image("ui/check_icon.png");
        crossImage = new Image("ui/x_icon.png");
        searchMethodHashMap.put("Full name", SearchMethod.FULL_NAME);
        searchMethodHashMap.put("Name", SearchMethod.NAME);
        searchMethodHashMap.put("Last name", SearchMethod.LAST_NAME);
        searchMethodHashMap.put("Code", SearchMethod.CODE);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getResultsBTN.setDisable(true);
        progressIndicator.setVisible(false);

        // Checks the status of the loaded logs to disable or enable buttons
        updateLoadStatus();


        // Converts the HashMap keys to an array in order to add them to the ComboBox
        String[] arr = {"Full name", "Name", "Last name", "Code"};
        searchCBX.getItems().setAll(arr);

        searchBar.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Operates the searchbar if the ComboBox criteria is different from null
                if (searchCBX.getValue() != null && searchBar.getText().length() > 0) {
                    try {
                        SearchMethod searchMethod = searchMethodHashMap.get(searchCBX.getValue());
                        operateSearchBar(searchMethod);
                    } catch (NumberFormatException e) {
                        coincidentRecords.clear();
                    }
                } else {
                    coincidentRecords.clear();
                }

                // Once the ArrayList of coincidences is full, fill the ListView with ObservableList
                ObservableList<Person> coincidentRecordsObs = FXCollections.observableArrayList(coincidentRecords);
                emergentList.setItems(coincidentRecordsObs);

                //Check if the Results Button can be enabled or not
                getResultsBTN.setDisable(coincidentRecords.size() > MIN_ALLOWED_COINCIDENCES_RESULTS || coincidentRecords.size() <= 0);
            }
        });
    }

    public void operateSearchBar(SearchMethod searchMethod) {
        coincidentRecords = new ArrayList<>();
        String stringToFind = searchBar.getText();
        Comparator<Person> findComp;
        Person pToFind;
        Node<Person> foundNode;

        switch (searchMethod) {
            case FULL_NAME:
                findComp = (A, B) -> {
                    int lengthSubstring = stringToFind.length();

                    // Assure that the substring indexes do not overflow
                    if (A.getFullName().length() < lengthSubstring) {
                        lengthSubstring = A.getFullName().length();
                    }

                    if (B.getFullName().length() < lengthSubstring){
                        lengthSubstring = B.getFullName().length();
                    }

                    String subFullNameA = A.getFullName().substring(0, lengthSubstring);
                    String subFullNameB = B.getFullName().substring(0, lengthSubstring);

                    return subFullNameA.compareTo(subFullNameB);
                };

                pToFind = new Person(stringToFind);
                foundNode = Database.fullNameAVLTree.find(pToFind, findComp);

                // Fill the coincidences ArrayList if a node was found
                if (foundNode != null) {
                    subTreePreorder(foundNode, MAX_SHOWN_COINCIDENCES, findComp, foundNode.getValue());
                } else {
                    coincidentRecords.clear();
                }

                break;

            case NAME:
                findComp = (A, B) -> {
                    String[] namesA = A.getFullName().split(" ");
                    String[] namesB = B.getFullName().split(" ");

                    int lengthSubstring = stringToFind.length();

                    // Assure that the substring indexes do not overflow
                    if (namesA[0].length() < lengthSubstring) {
                        lengthSubstring = namesA[0].length();
                    }

                    if (namesB[0].length() < lengthSubstring){
                        lengthSubstring = namesB[0].length();
                    }

                    String subNameA = namesA[0].substring(0, lengthSubstring);
                    String subNameB = namesB[0].substring(0, lengthSubstring);

                    return subNameA.compareTo(subNameB);
                };

                pToFind = new Person(stringToFind + " *");
                foundNode = Database.nameAVLTree.find(pToFind, findComp);

                // Fill the coincidences ArrayList if a node was found
                if (foundNode != null) {
                    subTreePreorder(foundNode, MAX_SHOWN_COINCIDENCES, findComp, foundNode.getValue());
                } else {
                    coincidentRecords.clear();
                }

                break;

            case LAST_NAME:
                findComp = (A, B) -> {
                    String[] namesA = A.getFullName().split(" ");
                    String[] namesB = B.getFullName().split(" ");

                    int lengthSubstring = stringToFind.length();

                    // Assure that the substring indexes do not overflow
                    if (namesA[1].length() < lengthSubstring) {
                        lengthSubstring = namesA[1].length();
                    }

                    if (namesB[1].length() < lengthSubstring){
                        lengthSubstring = namesB[1].length();
                    }

                    String subLastNameA = namesA[1].substring(0, lengthSubstring);
                    String subLastNameB = namesB[1].substring(0, lengthSubstring);

                    return subLastNameA.compareTo(subLastNameB);
                };

                pToFind = new Person("* " + stringToFind);
                foundNode = Database.lastNameAVLTree.find(pToFind, findComp);

                // Fill the coincidences ArrayList if a node was found
                if (foundNode != null) {
                    subTreePreorder(foundNode, MAX_SHOWN_COINCIDENCES, findComp, foundNode.getValue());
                } else {
                    coincidentRecords.clear();
                }

                break;

            case CODE:
                int codeToFind = Integer.parseInt(stringToFind);

                findComp = (A, B) -> {
                    int lengthSubstring = stringToFind.length();

                    // Assure that the substring indexes do not overflow
                    if (String.valueOf(A.getCode()).length() < lengthSubstring) {
                        lengthSubstring = String.valueOf(A.getCode()).length();
                    }

                    if(String.valueOf(B.getCode()).length() < lengthSubstring){
                        lengthSubstring = String.valueOf(B.getCode()).length();
                    }

                    String subCodeA = String.valueOf(A.getCode()).substring(0, lengthSubstring);
                    String subCodeB = String.valueOf(B.getCode()).substring(0, lengthSubstring);

                    return subCodeA.compareTo(subCodeB);
                };

                pToFind = new Person(codeToFind);
                foundNode = Database.codeAVLTree.find(pToFind, findComp);

                // Fill the coincidences ArrayList if a node was found
                if (foundNode != null) {
                    subTreePreorder(foundNode, MAX_SHOWN_COINCIDENCES, findComp, foundNode.getValue());
                } else {
                    coincidentRecords.clear();
                }

                break;
        }
    }

    // Uses preorder traversal to get the related records to display on the TableView
    public void subTreePreorder(Node<Person> node, int cnt, Comparator<Person> c, Person foundPerson){
        // Return conditions
        if (node == null) {
            return;
        }
        if (cnt == 0) {
            return;
        }

        // Add to the coincident list depending on the comparator
        if (c.compare(foundPerson, node.getValue()) == 0) {
            coincidentRecords.add(node.getValue());
            cnt--;
        }

        // Go through left subtree
        subTreePreorder(node.getLeft(), cnt, c, foundPerson);

        // Go through right subtree
        subTreePreorder(node.getRight(), cnt, c, foundPerson);
    }

    // Checks the status of the loaded logs to disable or enable buttons
    public void updateLoadStatus() {
        verificationImage.setVisible(true);
        progressIndicator.setVisible(false);

        if (!loadedData) {
            verificationImage.setImage(crossImage);
            statusLabel.setText("Logs not loaded");
            deleteLogsITEM.setDisable(true);
            saveITEM.setDisable(true);
            saveExitITEM.setDisable(true);

            // Checks the status of the JSON file
            if (arePrevLogsAvailable()) {
                loadPrevITEM.setDisable(false);
            } else {
                loadPrevITEM.setDisable(true);
                deletePrevLogsITEM.setDisable(true);
            }
        } else {
            verificationImage.setImage(checkImage);
            statusLabel.setText("Logs loaded");
            deleteLogsITEM.setDisable(false);
            saveITEM.setDisable(false);
            saveExitITEM.setDisable(false);
            loadPrevITEM.setDisable(true);

            if (!arePrevLogsAvailable()) {
                deletePrevLogsITEM.setDisable(true);
            } else {
                deletePrevLogsITEM.setDisable(false);
            }
        }
    }

    // Checks if the previous logs are available
    public boolean arePrevLogsAvailable() {
        boolean flag = true;

        try {
            FileInputStream fis = new FileInputStream(new File("data/logs.json"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;

            if ((line = reader.readLine()) == null) {
                flag = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    @FXML
    void addRecord(ActionEvent event) {

    }

    @FXML
    void exitSaveJSON(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit and save logs");
        alert.setContentText("Are you sure that you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            saveITEM.fire();
            System.exit(0);
        }
    }

    @FXML
    void deleteLogs(ActionEvent event) throws FileNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete actual logs");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            Database.codeAVLTree.clearTree();
            Database.fullNameAVLTree.clearTree();
            Database.nameAVLTree.clearTree();
            Database.lastNameAVLTree.clearTree();

            loadedData = false;
            updateLoadStatus();
        }
    }

    @FXML
    void deletePrevLogs(ActionEvent event) throws FileNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete previous logs");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            PrintWriter pw = new PrintWriter("data/logs.json");
            pw.close();

            loadPrevITEM.setDisable(true);
            updateLoadStatus();
        }
    }

    @FXML
    void generateLogs(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/GenerateWindow.fxml"));
        loader.setController(new GenerateController());
        Parent parent = (Parent) loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
        Stage s = (Stage) getResultsBTN.getScene().getWindow();
        s.close();
    }

    @FXML
    void getResults(ActionEvent event) {

    }

    @FXML
    void clearSearchBar(ActionEvent event) {
        searchBar.clear();
    }

    @FXML
    void saveJSON(ActionEvent event) {

        if (loadedData) {
            Database database = new Database();
            statusLabel.setText("Saving data...");

            new Thread(() -> {
                // Indicates that the process is in progress
                verificationImage.setVisible(false);
                progressIndicator.setVisible(true);

                database.generatePreorderArray();
                database.saveJSON();

                loadedData = true;

                Platform.runLater(this::updateLoadStatus);
            }).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There are not any logs at the moment");
        }

    }

    @FXML
    void loadJSON(ActionEvent event) {
        Database database = new Database();

        statusLabel.setText("Loading data...");
        loadPrevITEM.setDisable(true);

        new Thread(() -> {
            // Indicates that the process is in progress
            verificationImage.setVisible(false);
            progressIndicator.setVisible(true);

            database.loadJSON();

            loadedData = true;
            progressIndicator.setVisible(false);
            Platform.runLater(this::updateLoadStatus);
        }).start();
    }
}