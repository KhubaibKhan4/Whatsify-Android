package com.codespacepro.whatsify.Models;

public class Users {
    String username, fullname, email, password, gender;
    int pic;

    public Users() {
    }

    public Users(String username, String fullname, String email, String password, String gender, int pic) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.pic = pic;
    }

    public Users(String username, String fullname, String email, String password, String gender) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.gender = gender;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
