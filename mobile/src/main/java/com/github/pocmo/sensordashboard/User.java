package com.github.pocmo.sensordashboard;

import java.io.Serializable;

/**
 * author AHJ
 */
public class User implements Serializable{
    private String username;
    private String email;
    private String isVS;

    public User() {
        // Default constructor required for calls to DragonSnapshot.getValue(User.class)
    }

    public User(String username, String email, String isVS) {
        super();
        this.username = username;
        this.email = email;
        this.isVS = isVS;
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
}
