package model;

import control.GenerateController;
import exception.DuplicateValueException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Person testPerson;

    public void setupStage1() throws DuplicateValueException {
        testPerson = new Person("Maria Valderrama", "Female", LocalDate.now(), 1.9, "Peru", 4);

        Database.addPerson(new Person("Federico Hernandez", "Male", LocalDate.now(), 1.5, "Venezuela", 1));
        Database.addPerson(new Person("John Wayne", "Male", LocalDate.now(), 1.7, "United States", 2));
        Database.addPerson(new Person("Will Smith", "Male", LocalDate.now(), 1.58, "Canada", 3));
        Database.addPerson(testPerson);
    }

    public void setupStage2(){
        Database.clearTrees();
    }

    @Test
    void addPerson() throws DuplicateValueException {
        setupStage2();
        Database.addPerson(testPerson);

        // Check that Maria is in the root of every tree
        assertEquals(Database.fullNameAVLTree.getRoot().getValue(), testPerson);
        assertEquals(Database.lastNameAVLTree.getRoot().getValue(), testPerson);
        assertEquals(Database.nameAVLTree.getRoot().getValue(), testPerson);
        assertEquals(Database.codeAVLTree.getRoot().getValue(), testPerson);
    }

    @Test
    void deletePerson() throws DuplicateValueException {
        setupStage1();

        // Delete Maria Valderrama
        Database.deletePerson(testPerson);

        boolean found = false;

        Database database = new Database();
        database.generatePreorderArray();
        ArrayList<Person> avlLogs = database.getAvlLogs();

        // Search for Maria in the generated array
        for (Person p :
                avlLogs) {
            if (p.equals(testPerson)) {
                found = true;
            }
        }

        // If it was not found, Maria was deleted
        assertFalse(found);
    }

    @Test
    void generateRecords() {
        setupStage2();

        // Generate the random records
        Database database = new Database(5);
        database.generateRecords(null);

        assertEquals(Database.fullNameAVLTree.getSize(), 5);
        assertEquals(Database.nameAVLTree.getSize(), 5);
        assertEquals(Database.lastNameAVLTree.getSize(), 5);
        assertEquals(Database.codeAVLTree.getSize(), 5);
    }
}