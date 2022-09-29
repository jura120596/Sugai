package ru.sugai.village.data;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event implements Comparable<Event> {

    private int id;
    private String title;
    private String place;
    private String date;
    private String created_at;
    private String updated_at;
    private int points;
    private News data;

    @Override
    public String toString() {
        return "EventJSON{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", date='" + date + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", points=" + points +
                ", data=" + data +
                '}';
    }

    public News getData() {
        return data;
    }

    public void setData(News data) {
        this.data = data;
    }

    public int getId() {
        return id;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static boolean canAddParticipant(Event e) {
        if (!e.getDate().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                try {
                    Date c = sdf.parse(e.getDate());
                    long now = (new Date()).getTime();
                    long eventTime = c.getTime();
                    return eventTime < (now) && eventTime + 7200000 > now;
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return false;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Map<String, String> addParticipant(Context context, String participant_card_id) {
        Map<String, String> e = new HashMap<>();
        e.put("participant_card_id", participant_card_id);
        addParticipant(context, e);
        return e;
    }

    public Map<String, String> addParticipant(Context context, Integer participant_id) {
        Map<String, String> e = new HashMap<>();
        e.put("participant_id", participant_id == null ? "0" : participant_id + "");
        addParticipant(context, e);
        return e;
    }

    public void addParticipant(Context context, Map<String, String> map) {
        Call<ResponseBody> addPointsNFC = Retrofit.getInstance().getApi().addEventParticipant("Bearer " + DataBASE.token, getId(), map);
        addPointsNFC.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    try {
                        Log.d(CONST.SERVER_LOG, "ADD Participant " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Пользователь или метка не найдены", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Метка не поддерживается", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int compareTo(Event event) {
        return Integer.compare(id, event.getId());
    }
}
