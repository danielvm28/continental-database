package main;

import control.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;

public class Main extends Application{
    public static void main(String[] args) {
        Database database = new Database();
        database.loadJSON();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("../ui/MainWindow.fxml"));
        loader.setController(new MainController());
        Parent parent = (Parent) loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
