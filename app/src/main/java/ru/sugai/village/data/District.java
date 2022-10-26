package ru.sugai.village.data;

public class District {
    private String name;
    private int id;
    private int parent_district_id;
    private District region;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_district_id() {
        return parent_district_id;
    }

    public void setParent_district_id(int parent_district_id) {
        this.parent_district_id = parent_district_id;
    }

    public District getRegion() {
        return region;
    }
    public void setRegion(District region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return  name;
    }
}
