package control;
import exception.NameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class addRecordController implements Initializable {
     @FXML
     private Button buttomBack;

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
     private TextField textFieldHeigth;

     @FXML
     private TextField textFieldNationality;

     @Override
     public void initialize(URL location, ResourceBundle resources) {
          String[] comboBoxString = {"Man", "Women", "Others"};
          comboBoxGender.getItems().setAll(comboBoxString);
     }

     @FXML
     void addButom(ActionEvent event) throws NameException {
          String txFullName = textFieldFullName.getText();
          String message = "";
          Alert alert;
          alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");


          if (txFullName.split(" ").length<= 2){
               alert.setHeaderText("Problems with the full name");
               alert.setContentText("Remember that the full name can only contain the first name and the last name");
          } else {
               if (textFieldFullName.getText().isEmpty()){
                    message += "The full name is empty"+"\n";
               }
               if (comboBoxGender.getItems().isEmpty()){
                    message += "The gender is empty"+"\n";
               }
               if (dateBirthDate.getValue()==null){
                    message += "The Date picker is empty"+"\n";
               }
               if (textFieldHeigth.getText().isEmpty()){
                    message += "The height is empty"+"\n";
               }
               if (textFieldNationality.getText().isEmpty()){
                    message += "The nationality is empty"+"\n";
               }
               alert.setHeaderText("Problem with some data");
               alert.setContentText(message);
          }
          alert.show();
     }
}
