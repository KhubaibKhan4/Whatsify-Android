package com.codespacepro.whatsify.Models;

public class Users {
    String username, fullname, email, password, gender;
    String pic;
    String cover;

    public Users() {
    }

    public Users(String username, String fullname, String email, String password, String gender, String pic, String cover) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.pic = pic;
        this.cover = cover;
    }



    public Users(String username, String fullname, String email, String password, String gender) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.gender = gender;

    }

    public Users(String username, String fullname, String email, String gender) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;

    }



    public Users( String fullname, String pic) {
        this.fullname = fullname;
        this.pic = pic;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
