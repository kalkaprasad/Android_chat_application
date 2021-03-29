package com.lms.deepakchatapp.main;

public class ContactModel
{

      String username;
     String userprofile;
  String status;
    public ContactModel(String username, String userprofile, String status) {
        this.username = username;
        this.userprofile = userprofile;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(String userprofile) {
        this.userprofile = userprofile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
