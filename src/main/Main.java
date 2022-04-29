package main;

import control.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadMainWindow();
    }

    public static void loadMainWindow() throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/MainWindow.fxml"));
        loader.setController(new MainController());
        Parent parent = (Parent) loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}