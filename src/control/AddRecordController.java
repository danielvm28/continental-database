package control;
import exception.DuplicateValueException;
import exception.NameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
     private TextField textFieldHeigth;

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
     void addButton(ActionEvent event) throws NameException, DuplicateValueException {
          String txFullName = textFieldFullName.getText();
          String message = "";
          Alert alert;
          alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          boolean negative = false;

          if (txFullName.split(" ").length> 2){
               alert.setHeaderText("Problems with the full name");
               alert.setContentText("Remember that the full name can only contain the first name and the last name");
               alert.show();
               negative = true;
          } else {
               if (textFieldFullName.getText().isEmpty()){
                    message += "The full name is empty"+"\n";
                    negative = true;
               }
               if (comboBoxGender.getItems().isEmpty()){
                    message += "The gender is empty"+"\n";
                    negative = true;
               }
               if (dateBirthDate.getValue()==null){
                    message += "The Date picker is empty"+"\n";
                    negative = true;
               }
               if (textFieldHeigth.getText().isEmpty()){
                    message += "The height is empty"+"\n";
                    negative = true;
               }
               if (textFieldNationality.getText().isEmpty()){
                    message += "The nationality is empty"+"\n";
                    negative = true;
               }
               if (negative){
                    alert.setHeaderText("Problem with some data");
                    alert.setContentText(message);
                    alert.show();
               } else {
                    try {
                         Database database = new Database();
                         try {
                              database.addPerson(txFullName, comboBoxGender.getValue(), dateBirthDate.getValue(),
                                      Double.parseDouble(textFieldHeigth.getText()), textFieldNationality.getText(), Integer.parseInt(labelCode.getText()));
                         } catch (Exception e){
                              alert.setHeaderText("Problems with the height");
                              alert.setContentText("The height variable cannot contain letters or special characters. ");
                              alert.show();
                              negative = true;
                         }

                         if (!negative){
                              FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/MainWindow.fxml"));
                              loader.setController(new MainController());
                              Parent parent = (Parent) loader.load();
                              Stage stage = new Stage();
                              Scene scene = new Scene(parent);
                              stage.setScene(scene);
                              stage.show();
                              Stage s = (Stage) buttonAddRecord.getScene().getWindow();
                              s.close();
                         }
                    } catch (IOException e) {
                         e.printStackTrace();
                    }
               }

          }

     }

     @FXML
     void back(ActionEvent event) throws IOException {
          FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/MainWindow.fxml"));
          loader.setController(new MainController());
          Parent parent = (Parent) loader.load();
          Stage stage = new Stage();
          Scene scene = new Scene(parent);
          stage.setScene(scene);
          stage.show();
          Stage s = (Stage) buttonBack.getScene().getWindow();
          s.close();
     }
}
