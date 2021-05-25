package app;

import GUI.GUI;
import config.SetUpDataUsingStatement;

public class App
{
    public static void main(String[] args) {
        SetUpDataUsingStatement statement = new SetUpDataUsingStatement();
        statement.createUserTable();
        statement.createLocationTable();
        statement.createEventTable();
        statement.createArtistTable();
        statement.createArtistEventTable();
        statement.createTicketTypeTable();
        statement.createTicketTable();
        GUI.getInstance();
    }
}
