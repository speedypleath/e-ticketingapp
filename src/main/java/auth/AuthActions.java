package auth;

public interface AuthActions {
    boolean login(String username, String password);
    void setNextAction(AuthActions nextAction);
    void logout();
}
