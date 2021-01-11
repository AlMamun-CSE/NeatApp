package com.techdsf.neatapp.Models;

public class Users {
    private String userId,userImage,userName,userEmail,userPassword,userLastMessage;

    //Empty Constructor
    public Users(){}

    //Users All Property Constructor
    public Users(String userId, String userImage, String userName, String userEmail, String userPassword, String userLastMessage) {
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userLastMessage = userLastMessage;
    }

    //SignUp Constructor
    public Users(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLastMessage() {
        return userLastMessage;
    }

    public void setUserLastMessage(String userLastMessage) {
        this.userLastMessage = userLastMessage;
    }
}
