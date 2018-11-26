package com.github.pocmo.sensordashboard;

/**
 * author AHJ
 */
public class User {
    private String username;
    private String email;

    public User() {
        // Default constructor required for calls to DragonSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;

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


}
