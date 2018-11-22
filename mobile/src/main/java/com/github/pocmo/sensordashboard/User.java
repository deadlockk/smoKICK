package com.github.pocmo.sensordashboard;

import android.net.Uri;

/**
 * author AHJ
 */
public class User {
    private String username;
    private String email;
    private String photoURL;

    public User() {
        // Default constructor required for calls to DragonSnapshot.getValue(User.class)
    }

    public User(String username, String email, String photoURL) {
        this.username = username;
        this.email = email;
        this.photoURL = photoURL;
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

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
