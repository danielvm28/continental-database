package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import main.Main;
import model.Database;

import java.io.IOException;

public class MainController {
    @FXML
    private MenuItem loadPrevITEM;

    @FXML
    private MenuItem saveITEM;

    @FXML
    private MenuItem saveExitITEM;

    @FXML
    private MenuItem generateITEM;

    @FXML
    private MenuItem addITEM;

    @FXML
    private ComboBox<?> searchCBX;

    @FXML
    private ListView<?> emergentList;

    @FXML
    private Button getResultsBTN;

    @FXML
    void addRecord(ActionEvent event) {

    }

    @FXML
    void exitSaveJSON(ActionEvent event) {

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
    void saveJSON(ActionEvent event) {
        Database database = new Database();
        database.generatePreorderArrays();
        database.saveJSON();
        System.out.println("done");
    }

    @FXML
    void loadJSON(ActionEvent event) {
        Database database = new Database();
        database.loadJSON();
        System.out.println("done");
    }

}
