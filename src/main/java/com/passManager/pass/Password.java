package com.passManager.pass;
import io.vertx.core.json.JsonObject;

public class Password {
    private String username;
    private String password;
    private String website;
    private String email;
    private int id;

    public Password(int id, String username, String password, String website, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.website = website;
        this.email = email;
    }

    public Password(JsonObject pass) {
        this.id = pass.getInteger("id");
        this.username = pass.getString("username");
        this.password = pass.getString("password");
        this.website = pass.getString("website");
        this.email = pass.getString("email");
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {

        JsonObject pass = new JsonObject();
        pass.put("id", this.id);
        pass.put("username", this.username);
        pass.put("password", this.password);
        pass.put("website", this.website);
        pass.put("email", this.email);

        return pass.encodePrettily();
    }
}
