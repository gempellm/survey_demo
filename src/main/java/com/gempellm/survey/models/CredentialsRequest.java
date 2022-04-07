package com.gempellm.survey.models;

import lombok.NonNull;

public class CredentialsRequest {
    private @NonNull String login;
    private @NonNull String password;
    private @NonNull String roles;

    public CredentialsRequest() {
    }

    public CredentialsRequest(String login, String password, String roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
