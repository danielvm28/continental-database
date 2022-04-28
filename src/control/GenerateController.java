package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Database;

import java.io.IOException;

public class GenerateController {
    @FXML
    private TextField recordsTF;

    @FXML
    private Button generateBTN;

    @FXML
    private Button backBTN;

    @FXML
    void generateRecords(ActionEvent event) {
        Alert alert = new Alert(AlertType.WARNING);

        try{
            if(!recordsTF.getText().trim().isEmpty() && Integer.parseInt(recordsTF.getText().trim()) > Database.MAX_RECORDS){
                alert.setTitle("Warning");
                alert.setHeaderText("Maximum logs exceeded");
                alert.setContentText("The maximum amount of records allowed is one million");

                alert.show();
                recordsTF.clear();
            } else {
                Database database;

                if (recordsTF.getText().trim().isEmpty()){
                    database = new Database();
                } else {
                    database = new Database(Integer.parseInt(recordsTF.getText().trim()));
                }

                //TODO go to progress bar

                database.generateRecords();
                backBTN.fire();
            }
        } catch (NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText("Wrong number format");
            alert.setContentText("Please use integers");

            alert.show();
            recordsTF.clear();
        }

    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Main.loadMainWindow();

        Stage s = (Stage) backBTN.getScene().getWindow();
        s.close();
    }
}
