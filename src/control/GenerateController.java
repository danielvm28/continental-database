package control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;
import model.Database;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class GenerateController implements Initializable {
    @FXML
    private TextField recordsTF;

    @FXML
    private Button generateBTN;

    @FXML
    private Button backBTN;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button generateDefBTN;

    @FXML
    private Label progressPercentageText;

    @FXML
    private ImageView checkIcon;

    @FXML
    private Text timeText;

    private boolean finishedGeneration;

    private Instant startGenerationTime;

    private int percentageCounter;

    public GenerateController() {
        finishedGeneration = false;
    }

    @FXML
    void generateRecords(ActionEvent event) {
        Alert alert;
        try{
            if (recordsTF.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Generation text field empty");
                alert.setContentText("Please type the amount of records to generate");

                alert.show();
            } else if(Integer.parseInt(recordsTF.getText().trim()) > Database.MAX_RECORDS){
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Maximum logs exceeded");
                alert.setContentText("The maximum amount of records allowed is one million");

                alert.show();
                recordsTF.clear();
            } else {
                Database database = new Database(Integer.parseInt(recordsTF.getText().trim()));
                callThreads(database);
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

    // Calls the threads that control the progress bar and its indicators
    private void callThreads(Database database) {
        startGenerationTime = Instant.now();

        // Sets visible the indicators and invisible the icon
        timeText.setVisible(true);
        progressPercentageText.setVisible(true);
        checkIcon.setVisible(false);

        // Disables the generation buttons to avoid threading conflicts
        generateDefBTN.setDisable(true);
        generateBTN.setDisable(true);

        // Calls the thread to control the counter
        new Thread(() -> {
            while (!finishedGeneration) {
                long dif = ChronoUnit.SECONDS.between(startGenerationTime, Instant.now());
                timeText.setText(dif + " s");
            }
        }).start();

        // Calls the thread to control the progress bar
        new Thread(() -> {
            database.generateRecords(this);
            finishedGeneration = true;

            // Enables the buttons and shows the check icon
            generateDefBTN.setDisable(false);
            generateBTN.setDisable(false);
            checkIcon.setVisible(true);
        }).start();

        MainController.loadedData = true;
    }

    @FXML
    void generateDefault(ActionEvent event) {
        Database database = new Database();
        callThreads(database);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Main.loadMainWindow();

        Stage s = (Stage) backBTN.getScene().getWindow();
        s.close();
    }

    public void updateProgressBar(double progress) {
        int progressInt = (int) (progress * 100);
        progressBar.setProgress(progress);

        if (progressInt != percentageCounter) {
            String percentage = progressInt + "%";

            Platform.runLater(() -> {
                progressPercentageText.setText(percentage);
            });

            percentageCounter = progressInt;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateDefBTN.setText("Generate " + Database.MAX_RECORDS + " records");
        timeText.setVisible(false);
        progressPercentageText.setVisible(false);
        checkIcon.setVisible(false);
    }
}
