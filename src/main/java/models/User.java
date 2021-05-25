package models;

import utility.CSV;

public abstract class User implements CSV
{
    private String salt;
    private final String username;
    private String name;
    private String email;
    private String password;

    public User(String salt, String username, String password, String email, String name){
        this.salt = salt;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    User(User user){
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.name = user.name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "salt=" + salt +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public String toCSV() {
        return username + "," + password + "," + salt + "," + name + "," + email;
    }
}