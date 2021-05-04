package service;

public class Validator
{
    public static boolean validateEmail(String email){
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.replaceAll(" ","").matches(regex);
    }

    public static boolean validateName(String name){
        String regex = "^[A-Z][a-z]+$";
        return name.matches(regex);
    }

    public static boolean validatePassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&-+=().])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }
}
