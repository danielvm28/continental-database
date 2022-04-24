package main;

import control.MainController;
import exception.DuplicateValueException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.AVLTree;
import model.Database;
import model.Node;
import model.Person;

import java.time.LocalDate;
import java.util.Comparator;

public class Main extends Application{
    public static void main(String[] args) throws DuplicateValueException {
        AVLTree<String> avl = new AVLTree<>();

//        Comparator<Person> c = (a, b) -> {
//            String[] fullNameA = a.getFullName().split(" ");
//            String[] fullNameB = b.getFullName().split(" ");
//
//            return fullNameA[1].compareTo(fullNameB[1]);
//        };
//
//        avl.insert(new Person("Diego Calle", "Male", LocalDate.now(), 2, "Cuba", "A"), c);
//        avl.insert(new Person("Maria Rodi", "Male", LocalDate.now(), 2, "Cuba", "B"), c);
//        avl.insert(new Person("Juan Camilo", "Male", LocalDate.now(), 2, "Cuba", "C"), c);
//        avl.insert(new Person("Pedro Calencia", "Male", LocalDate.now(), 2, "Cuba", "D"), c);
//
//        Node<Person> found = avl.find(new Person("Hola Calle", "Male", LocalDate.now(), 2, "Cuba", "A"), c);
//        System.out.println(found.getValue().getFullName());
//        avl.print2D();
//
//        Database.generateRecords();

        avl.insert("JKB458");
        avl.insert("ESP526");
        avl.insert("GUI512");
        avl.insert("TRM323");
        avl.insert("QEE419");
        avl.delete("GUI512");
        avl.insert("SAL422");
        avl.delete("ESP526");
        avl.insert("OPS437");
        avl.delete("TRM323");

        avl.print2D();


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
