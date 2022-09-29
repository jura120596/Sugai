package ru.sugai.village.data;

public class Author {

    private int id;
    private String name;
    private String last_name;
    private String second_name;
    private String full_name;
    private boolean curator;

    public Author() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public boolean isCurator() {
        return curator;
    }

    public void setCurator(boolean curator) {
        this.curator = curator;
    }
}
