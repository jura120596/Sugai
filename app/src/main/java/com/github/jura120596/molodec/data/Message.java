package com.github.jura120596.molodec.data;

import com.github.jura120596.molodec.data.database.DataBASE;

import java.io.Serializable;

public class Message implements Comparable<Message>, Serializable {
    private int id;
    private int role;
    private int user_id;
    private String text = "";
    private String created_at="";
    private String updated_at="";
    private User user = null;
    private String date = "";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public boolean fromOtherUser() {
        boolean notLibr = !user.isLibrarian();
        boolean notAdmin = !user.isAdmin();
        boolean otherUser = user.getId() != DataBASE.user.getId();
        boolean b = otherUser && DataBASE.user.isUser() || otherUser &&  notAdmin && notLibr;
        return b;
    }

    @Override
    public int compareTo(Message userRequest) {
        return Integer.compare(this.id, userRequest.id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", role=" + role +
                ", user_id=" + user_id +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
