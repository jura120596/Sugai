package com.github.jura120596.molodec.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.jura120596.molodec.adapter.PhotosPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class News {
    protected int id;
    protected String title ="";
    protected String description="";
    protected int user_id;
    protected String created_at;
    protected String updated_at;
    protected ArrayList<Photo> photos = new ArrayList<Photo>();
    protected User author;
    protected String date ="";
    PhotosPagerAdapter adapter;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }
    public String getPathId() {
        return id > 0 ? "" + id : "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void initAdapter(Context context){
        if (context == null) return;
        adapter = new PhotosPagerAdapter(context, getPhotos());
    }

    public PhotosPagerAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user_id=" + user_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", photos=" + photos +
                ", author=" + author +
                ", date='" + date + '\'' +
                '}';
    }

    public List<MultipartBody.Part> getPhotosMultipart() {
        MultipartBody.Part photos = null;
        int i = 0;
        List<MultipartBody.Part> parts = new ArrayList<>();
        String name = "post_photos[]";
        for (Photo p : getPhotos()) {
            File file = new File(p.getFile());
            RequestBody filebody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            parts.add(MultipartBody.Part.createFormData(name,file.getName(),filebody));
            i++;
        }
        return parts;
    }
}



