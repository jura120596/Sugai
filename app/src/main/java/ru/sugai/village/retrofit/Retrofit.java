package ru.sugai.village.retrofit;

import ru.sugai.village.CONST.CONST;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private static final String BASE_URL = CONST.SERVER_URl;
    private static retrofit2.Retrofit retrofit;

    public static synchronized Retrofit getInstance(){
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();;
        }
        return new Retrofit();
    }

    public static Api getApi(){
        return retrofit.create(Api.class);
    }
}
