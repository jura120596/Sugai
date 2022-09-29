package ru.sugai.village.data;


import com.yandex.mapkit.map.PolygonMapObject;

import java.util.ArrayList;

public class MapObject {
    private int id;
    private String name;

    private ArrayList <ArrayList<Cords>> coords;
    private int points;
    private String type;
    private int app_type;
    private String color;
    private String created_at;
    private String updated_at;
    private int client_id;
    private PolygonMapObject polygonMapObject;


    public MapObject(String name) {
        this.name =name;
    }

    public MapObject() {
    }



    public ArrayList<ArrayList<Cords>> getCoords() {
        return coords;
    }

    public void setCoords(ArrayList<ArrayList<Cords>> coords) {
        this.coords = coords;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getApp_type() {
        return app_type;
    }

    public void setApp_type(int app_type) {
        this.app_type = app_type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
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


    @Override
    public String toString() {
        return "MapObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coords=" + coords +
                ", points=" + points +
                ", type='" + type + '\'' +
                ", app_type=" + app_type +
                ", color='" + color + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", client_id='" + client_id + '\'' +
                '}';
    }

    public PolygonMapObject getPolygonMapObject() {
        return polygonMapObject;
    }

    public void setPolygonMapObject(PolygonMapObject polygonMapObject) {
        this.polygonMapObject = polygonMapObject;
    }
}
