package control;

import exception.DuplicateValueException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Database;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class EditRecord implements Initializable {
    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonEdit;

    @FXML
    private ComboBox<String> comboBoxGender;

    @FXML
    private DatePicker datePickerBirthDate;

    @FXML
    private Label labelCode;

    @FXML
    private TextField textFullName;

    @FXML
    private TextField textHeight;

    @FXML
    private TextField textNationality;

    public static Person personD;

    public EditRecord(Person person) {
        personD = person;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] comboBoxString = {"Male", "Female", "Others"};
        comboBoxGender.getItems().setAll(comboBoxString);
        textFullName.setText(personD.getFullName());
        comboBoxGender.setValue(personD.getGender());
        datePickerBirthDate.setValue(personD.getBirthDate());
        textHeight.setText(personD.getHeight()+"");
        textNationality.setText(personD.getNationality());
        labelCode.setText(personD.getCode()+"");
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Main.loadMainWindow();
        Stage s = (Stage) buttonBack.getScene().getWindow();
        s.close();
    }

    @FXML
    void edit(ActionEvent event) throws DuplicateValueException, IOException {
        Database database = new Database();
        database.deletePerson(personD);
        database.addPerson(textFullName.getText(), comboBoxGender.getValue(), datePickerBirthDate.getValue(), Double.parseDouble(textHeight.getText()), textNationality.getText(), Integer.parseInt(labelCode.getText()));
        Main.loadMainWindow();
        Stage s = (Stage) buttonEdit.getScene().getWindow();
        s.close();
    }

}
