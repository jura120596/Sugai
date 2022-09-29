package ru.sugai.village.data;

import ru.sugai.village.data.database.DataBASE;

import java.io.Serializable;

public class Photo implements Serializable {
    private int id;
    private String file;
    private int user_post_id;
    private String created_at;
    private String updated_at;

    public Photo(String absolutePath) {
        id = 0;
        file  = absolutePath;
        user_post_id = DataBASE.user.getId();
        created_at = "";
        updated_at = "";
    }


    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", file='" + file + '\'' +
                ", user_post_id=" + user_post_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getUser_post_id() {
        return user_post_id;
    }

    public void setUser_post_id(int user_post_id) {
        this.user_post_id = user_post_id;
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
}
