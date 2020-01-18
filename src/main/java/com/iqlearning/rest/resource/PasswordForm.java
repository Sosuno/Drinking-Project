package com.iqlearning.rest.resource;

public class PasswordForm {
    private String currentPass;
    private String newPass;

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
