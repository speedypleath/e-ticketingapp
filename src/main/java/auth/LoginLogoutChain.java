package auth;

import GUI.GUI;
import service.MainService;

public class LoginLogoutChain implements AuthActions
{
    AuthActions actions;
    public LoginLogoutChain()
    {
        AuthActions gui = GUI.getInstance();
        AuthActions service = MainService.getInstance();
        this.actions = service;
        actions.setNextAction(gui);
    }

    @Override
    public boolean login(String username, String password) {

        return this.actions.login(username, password);
    }

    @Override
    public void setNextAction(AuthActions nextAction) {
        this.actions.setNextAction(nextAction);
    }

    @Override
    public void logout() {
        this.actions.logout();
    }
}
