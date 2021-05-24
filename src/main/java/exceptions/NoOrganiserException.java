package exceptions;

public class NoOrganiserException extends RuntimeException
{
    public NoOrganiserException(){super("Event doesn't have an organiser");}
}
