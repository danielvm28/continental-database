package control;
import exception.DuplicateValueException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.Database;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddRecordController implements Initializable {
     @FXML
     private Button buttonBack;

     @FXML
     private Button buttonAddRecord;

     @FXML
     private ComboBox<String> comboBoxGender;

     @FXML
     private DatePicker dateBirthDate;

     @FXML
     private Label labelCode;

     @FXML
     private TextField textFieldFullName;

     @FXML
     private TextField textFieldHeight;

     @FXML
     private TextField textFieldNationality;

     @Override
     public void initialize(URL location, ResourceBundle resources) {
          String[] comboBoxString = {"Male", "Female", "Others"};
          comboBoxGender.getItems().setAll(comboBoxString);
          Database database = new Database();
          labelCode.setText(database.generateCode()+"");
     }

     @FXML
     void addButton(ActionEvent event) throws DuplicateValueException {
          String txFullName = textFieldFullName.getText();
          String message = "";
          Alert alert;
          alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");

          if (txFullName.split(" ").length != 2){
               alert.setHeaderText("Problems with the full name");
               alert.setContentText("Remember that the full name can only contain the first name and the last name");
               alert.show();
          } else {
               if (textFieldFullName.getText().isEmpty()){
                    message += "The full name is empty"+"\n";
               }
               if (comboBoxGender.getValue()==null){
                    message += "The gender is empty"+"\n";
               }
               if (dateBirthDate.getValue()==null){
                    message += "The Date picker is empty"+"\n";
               }
               if (textFieldHeight.getText().isEmpty()){
                    message += "The height is empty"+"\n";
               }
               if (textFieldNationality.getText().isEmpty()){
                    message += "The nationality is empty"+"\n";
               }
               if (message.length()>0){
                    alert.setHeaderText("Problem with some data");
                    alert.setContentText(message);
                    alert.show();
               } else {
                    try {
                         Database database = new Database();
                         database.addPerson(txFullName, comboBoxGender.getValue(), dateBirthDate.getValue(),
                                 Double.parseDouble(textFieldHeight.getText()), textFieldNationality.getText(), Integer.parseInt(labelCode.getText()));

                         MainController.loadedData = true;

                         Main.loadMainWindow();

                         Stage s = (Stage) buttonAddRecord.getScene().getWindow();
                         s.close();
                    } catch (IOException e) {
                         e.printStackTrace();
                    } catch (NumberFormatException e){
                         alert.setHeaderText("Problems with the height");
                         alert.setContentText("The height variable cannot contain letters or special characters. ");
                         alert.show();
                    }
               }

          }
     }

     @FXML
     void back(ActionEvent event) throws IOException {
          Main.loadMainWindow();
          Stage s = (Stage) buttonBack.getScene().getWindow();
          s.close();
     }
}
