package structures;

import exception.DuplicateValueException;
import model.Database;
import model.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {
    private Person personToFind;

    private AVLTree<Person> avlTree;

    public void setupStage() throws DuplicateValueException {
        avlTree = new AVLTree<>();
        personToFind = new Person("Howard Bundy", "Male", LocalDate.now(), 1.30, "Colombia", 1);

        avlTree.insert(personToFind);
        avlTree.insert(new Person("Richard Miller", "Male", LocalDate.now(), 1.59, "Guatemala", 2));
        avlTree.insert(new Person("Duke Richard", "Male", LocalDate.now(), 1.87, "United States", 3));
        avlTree.insert(new Person("Salvatore Arrigo", "Male", LocalDate.now(), 1.33, "Bolivia", 4));
        avlTree.insert(new Person("David Smith", "Male", LocalDate.now(), 1.98, "Ecuador", 5));
        avlTree.insert(new Person("Maria Valencia", "Female", LocalDate.now(), 1.50, "Colombia", 6));
    }

    @Test
    void find() throws DuplicateValueException {
        setupStage();

        // Attempt to find Howard Bundy
        String stringToFind = "ho";

        // Copy of the Comparator used on the Main Window to make suggestions on the emergent list
        Comparator<Person> findComp = (A, B) -> {
            int lengthSubstringA = stringToFind.length();
            int lengthSubstringB = stringToFind.length();

            // Assure that the substring indexes do not overflow
            if (A.getFullName().length() < lengthSubstringA) {
                lengthSubstringA = A.getFullName().length();
            }

            if (B.getFullName().length() < lengthSubstringB){
                lengthSubstringB = B.getFullName().length();
            }

            String subFullNameA = A.getFullName().substring(0, lengthSubstringA).toLowerCase();
            String subFullNameB = B.getFullName().substring(0, lengthSubstringB).toLowerCase();

            return subFullNameA.compareTo(subFullNameB);
        };

        Person pToFind = new Person(stringToFind);
        Node<Person> foundNode = avlTree.find(pToFind, findComp);

        // Check if Howard Bundy was found
        assertEquals(foundNode.getValue(), personToFind);
    }
}