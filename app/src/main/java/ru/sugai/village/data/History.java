package ru.sugai.village.data;

public class History {

    private MapObject map_object;
    private Event event;
    private int id;
    private int user_id;
    private int map_object_id;
    private int village_event_id;
    private int points;
    private String created_at;
    private String updated_at;

    @Override
    public String toString() {
        return "HistoryJSON{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", map_object_id=" + map_object_id +
                ", points=" + points +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    public MapObject getMap_object() {
        return map_object;
    }

    public void setMap_object(MapObject map_object) {
        this.map_object = map_object;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMap_object_id() {
        return map_object_id;
    }

    public void setMap_object_id(int map_object_id) {
        this.map_object_id = map_object_id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getVillage_event_id() {
        return village_event_id;
    }

    public void setVillage_event_id(int village_event_id) {
        this.village_event_id = village_event_id;
    }
}
