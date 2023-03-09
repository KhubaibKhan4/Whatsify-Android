package com.codespacepro.whatsify.Models;

public class Chat {
    String profile, fullname,lastmessage;

    public Chat() {
    }

    public Chat(String profile, String fullname) {
        this.profile = profile;
        this.fullname = fullname;
    }

    public Chat(String profile, String name, String lastmessage) {
        this.profile = profile;
        this.fullname = name;
        this.lastmessage = lastmessage;

    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }


}
