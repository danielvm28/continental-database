package exception;

public class DuplicateValueException extends Exception{
    public DuplicateValueException() {
        super("This value is already in the tree, use another value");
    }
}
