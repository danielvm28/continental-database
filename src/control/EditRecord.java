package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] comboBoxString = {"Male", "Female", "Others"};
        comboBoxGender.getItems().setAll(comboBoxString);
    }
}
