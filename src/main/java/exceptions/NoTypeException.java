package exceptions;

public class NoTypeException extends RuntimeException
{
    public NoTypeException() {
        super("Event does not have a type");
    }
}
