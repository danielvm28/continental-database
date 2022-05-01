package exception;

public class NameException extends Exception{
    public NameException() {
        super("remember that the full name can only contain one first and one last name");
    }
}
