package com.passManager.pass;

public class Password {
    private String username;
    private String password;
    private String website;
    private String email;

    public Password(String username, String password, String website, String email) {
        this.username = username;
        this.password = password;
        this.website = website;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
