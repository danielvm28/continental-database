package control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    @FXML
    private Button backBTN;

    @FXML
    private TableColumn<Person, LocalDate> columBirthDate;

    @FXML
    private TableColumn<Person, String> columFullName;

    @FXML
    private TableColumn<Person, String> columGender;

    @FXML
    private TableColumn<Person, Double> columHeight;

    @FXML
    private TableColumn<Person, String> columNationality;

    @FXML
    private Button editBTN;

    @FXML
    private TableView<Person> resultsTV;

    @FXML
    void editRecord(ActionEvent event) throws IOException {
        if (stClicked!=null){
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/EditRecord.fxml"));
            loader.setController(new EditRecord(stClicked));
            Parent parent = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
            Stage s = (Stage) editBTN.getScene().getWindow();
            s.close();
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Main.loadMainWindow();
        Stage s = (Stage) backBTN.getScene().getWindow();
        s.close();
    }

    Person stClicked;
    ObservableList<Person> people;

    public ResultsController(ArrayList<Person> coincidentRecords) {
        people = FXCollections.observableArrayList(coincidentRecords);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        columGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        columHeight.setCellValueFactory(new PropertyValueFactory<>("height"));
        columNationality.setCellValueFactory(new PropertyValueFactory<>("nationality"));

        resultsTV.setItems(people);

        resultsTV.setOnMouseClicked(event -> {
            stClicked = resultsTV.getSelectionModel().getSelectedItem();
        });
    }
}
