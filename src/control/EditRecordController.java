package control;

import exception.DuplicateValueException;
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
        loadResultsWindow();
    }

    @FXML
    void edit(ActionEvent event) throws DuplicateValueException, IOException {
        Database.deletePerson(personD);

        // TODO faltan verificaciones antes de añadir a la nueva persona, alertas de Helicoptero apache y todo incluido
        String txFullName = textFullName.getText();
        String message = "";
        Alert alert;
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        if (txFullName.split(" ").length != 2){
            alert.setHeaderText("Problems with the full name");
            alert.setContentText("Remember that the full name can only contain the first name and the last name");
            alert.show();
        } else {
            if (txFullName.isEmpty()){
                message += "The full name is empty"+"\n";
            }
            if (comboBoxGender.getValue()==null){
                message += "The gender is empty"+"\n";
            }
            if (datePickerBirthDate.getValue()==null){
                message += "The Date picker is empty"+"\n";
            }
            if (textHeight.getText().isEmpty()){
                message += "The height is empty"+"\n";
            }
            if (textNationality.getText().isEmpty()){
                message += "The nationality is empty"+"\n";
            }
            if (message.length()>0){
                alert.setHeaderText("Problem with some data");
                alert.setContentText(message);
                alert.show();
            } else {
                personEdit = new Person(textFullName.getText(), comboBoxGender.getValue(), datePickerBirthDate.getValue(), Double.parseDouble(textHeight.getText()), textNationality.getText(), Integer.parseInt(labelCode.getText()));
                Database.addPerson(personEdit);

                editCoincidentRecords();
                loadResultsWindow();
            }
        }
    }

    public void editCoincidentRecords() {
        coincidentRecordsList.remove(personD);
        coincidentRecordsList.add(personEdit);
    }

    private void loadResultsWindow() throws IOException {
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
