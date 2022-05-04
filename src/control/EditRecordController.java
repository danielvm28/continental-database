package control;

import exception.DuplicateValueException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.ArrayList;
import java.util.ResourceBundle;


public class EditRecordController implements Initializable {
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

    private final Person personD;

    private Person personEdit;

    private final ArrayList<Person> coincidentRecordsList;

    public EditRecordController(Person person, ArrayList<Person> coincidentRecordsList) {
        this.coincidentRecordsList = coincidentRecordsList;
        personD = person;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] comboBoxString = {"Male", "Female", "Other"};
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
        loadResultsWIndow();
    }

    @FXML
    void edit(ActionEvent event) throws DuplicateValueException, IOException {
        Database.deletePerson(personD);

        // TODO faltan verificaciones antes de añadir a la nueva persona, alertas de Helicoptero apache y todo incluido

        personEdit = new Person(textFullName.getText(), comboBoxGender.getValue(), datePickerBirthDate.getValue(), Double.parseDouble(textHeight.getText()), textNationality.getText(), Integer.parseInt(labelCode.getText()));
        Database.addPerson(textFullName.getText(), comboBoxGender.getValue(), datePickerBirthDate.getValue(), Double.parseDouble(textHeight.getText()), textNationality.getText(), Integer.parseInt(labelCode.getText()));

        editCoincidentRecords();
        loadResultsWIndow();
    }

    public void editCoincidentRecords() {
        coincidentRecordsList.remove(personD);
        coincidentRecordsList.add(personEdit);
    }

    private void loadResultsWIndow() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/ResultsWindow.fxml"));
        loader.setController(new ResultsController(coincidentRecordsList));
        Parent parent = (Parent) loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();

        Stage s = (Stage) buttonBack.getScene().getWindow();
        s.close();
    }
}
