package com.github.jura120596.molodec.retrofit.call;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.jura120596.molodec.CONST.CONST;
import com.github.jura120596.molodec.retrofit.responses.ValidationResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ValidateCallback <T> implements Callback<T> {
    abstract public void onSuccess(Call<T> call, Response<T> response);
    public void handleError(Call<T> call, Throwable t) {}
    public void on500(Call<T> call, Response<T> response) {
        Toast.makeText(getContext(), "Ошибка сервера, попробуйте обновить приложение", Toast.LENGTH_SHORT).show();}
    public void on422(Call<T> call, Response<T> response, ValidationResponse errors) {
        Toast.makeText(getContext(), "Проверьте данные", Toast.LENGTH_LONG).show();

    }
    public void on401(Call<T> call, Response<T> response) {}
    abstract public Context getContext();
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        System.out.println(call.request().method() + " " + call.request().url().url() +" -> " + response.code());
        if (response.code() >= 500||response.code() == 404) {
            on500(call,response);
            return;
        } else if (response.code() == 403 || response.code() == 401 ) {
            Toast.makeText(getContext(), "Доступ запрещен. Попробуйте войти снова!", Toast.LENGTH_SHORT).show();
            on401(call, response);
            return;
        } else if (response.code() == 422) {
            try {
                ValidationResponse vr = new ValidationResponse();
                JSONObject jo = new JSONObject(response.errorBody().string());
                vr.setMessage(jo.getString("message"));
                jo = jo.getJSONObject("errors");
                Iterator<String> keys = jo.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONArray jArray = jo.getJSONArray(key);
                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){
                            String string = jArray.getString(i);
                            vr.addError(key, string);
                        }
                    }
                }
                Log.d(CONST.SERVER_LOG, "ERROR: " + vr.toString());
                on422(call, response, vr);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                on422(call, response, null);
            }
            return;
        }
        this.onSuccess(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(CONST.SERVER_LOG, "ERROR: " + t.getMessage());
        handleError(call, t);
    }
}