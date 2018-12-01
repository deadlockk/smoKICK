package com.github.pocmo.sensordashboard;

import java.io.Serializable;

/**
 * author AHJ
 */
public class User implements Serializable{
    private String username;
    private String email;
    private String isVS;
    private String betting;

    public User() {
        // Default constructor required for calls to DragonSnapshot.getValue(User.class)
    }

    public User(String username, String email, String isVS, String betting) {
        super();
        this.username = username;
        this.email = email;
        this.isVS = isVS;
        this.betting = betting;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsVS() {
        return isVS;
    }

    public void setIsVS(String isVS) {
        this.isVS = isVS;
    }

    public String getBetting() {
        return betting;
    }

    public void setBetting(String betting) {
        this.betting = betting;
    }
}
