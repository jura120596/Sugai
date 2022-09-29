package ru.sugai.village.data;

public class Appeal extends News{
    private String comment;
    public int state;
    public int likes;

    public int user_like;


    public Appeal() {

    }
    public Appeal(String theme, String content, String from, String time) {
        this.title =from;
        this.description =content;
        this.date =time;
        this.comment = theme;

    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLikes() {
        return likes;
    }
    public String getLikesLabel() {
        return "\uD83D\uDC9A " + likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getUser_like() {
        return user_like;
    }

    public void setUser_like(int user_like) {
        this.user_like = user_like;
    }

    @Override
    public String toString() {
        return "Appeal{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                ", state=" + state +
                ", likes=" + likes +
                ", user_like=" + user_like +
                ", author=" + author +
                ", photos=" + photos +
                '}';
    }
}
