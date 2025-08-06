package com.github.jura120596.molodec.retrofit;

import com.github.jura120596.molodec.data.Appeal;
import com.github.jura120596.molodec.data.BusJSON;
import com.github.jura120596.molodec.data.District;
import com.github.jura120596.molodec.data.Event;
import com.github.jura120596.molodec.data.History;
import com.github.jura120596.molodec.data.MapObject;
import com.github.jura120596.molodec.data.Message;
import com.github.jura120596.molodec.data.News;
import com.github.jura120596.molodec.data.ServerItemResponse;
import com.github.jura120596.molodec.data.ServerListResponse;
import com.github.jura120596.molodec.data.User;
import com.github.jura120596.molodec.data.UserRequest;
import com.github.jura120596.molodec.data.UserRequestType;
import com.github.jura120596.molodec.retrofit.responses.ObjectResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("api/auth/login")
    Call<JsonObject> login(@Field("email")String email, @Field("password") String password);

    @Headers({"Accept: application/json"})
    @POST("api/auth/signup")
    Call<ResponseBody> registration(@Body User user);

    @GET("api/district")
    Call<ServerListResponse<District>> loadDistricts(@Query("name") String name, @Query("level") Integer level, @Query("parent_district_id") Integer pid);
    @GET("api/district")
    Call<ServerListResponse<District>> loadDistricts(@Query("name") String name, @Query("level") Integer level);

    @POST("api/auth/logout")
    Call<ResponseBody> logout(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("api/auth/reset")
    Call<ResponseBody> resetPassword(@Field("email") String email);

    @POST("api/auth/profile")
    Call<ObjectResponse<User>> getProfile(@Header("Authorization") String authHeader);

    @POST("api/auth/profile")
    @Headers({"Accept: application/json"})
    Call<JsonObject> editProfile(@Header("Authorization") String authHeader,@Field("_method") String method, @Field("name") String name, @Field("second_name") String second_name,
                                 @Field("last_name")String last_name, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

    @POST("api/user/{user_id}")
    @Headers({"Accept: application/json"})
    Call<JsonObject> editProfile(@Header("Authorization") String authHeader,@Path("user_id") int id,@Query("_method") String method ,@Query("blocked") int block);

    @GET("api/user/event")
    Call<ServerListResponse<History>> getEventHistory(@Header("Authorization") String authHeader);





    @GET("api/type")
    Call<ServerListResponse<UserRequestType>> getRequestTypes();

    @POST("/api/type")
    @Headers({"Accept: application/json"})
    Call<ObjectResponse<UserRequestType>> addRequestType(@Header("Authorization") String authHeader, @Query("name") String name);

    @DELETE("api/type/{type_id}")
    Call<ResponseBody> deleteRequestType(@Header("Authorization") String authHeader, @Path("type_id") int id);





    @POST("api/post")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<Appeal>> addNews(@Header("Authorization") String authHeader, @Query("title") String title,@Query("description") String description);

    @POST("api/user/post")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<Appeal>> addAppeal(@Header("Authorization") String authHeader, @Query("title") String title,@Query("description") String description);


    @PUT("api/post/{post_id}")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<Appeal>> editNews(@Header("Authorization") String authHeader, @Path("post_id") String postID,@Query("title") String title, @Query("description") String description);

    @PUT("api/user/post/{post_id}")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<Appeal>> editAppeal(@Header("Authorization") String authHeader, @Path("post_id") String postID,@Query("title") String title, @Query("description") String description);

    @Multipart
    @POST("api/post/{post_id}")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<Appeal>> editNews(@Header("Authorization") String authHeader, @Path("post_id") String postID, @Query("_method") String put, @Query("title") String title, @Query("description") String description, @Part List<MultipartBody.Part> post_photos );

    @Multipart
    @POST("api/user/post/{post_id}")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<Appeal>> editAppeal(@Header("Authorization") String authHeader, @Path("post_id") String postID, @Query("_method") String put, @Query("title") String title, @Query("description") String description, @Part List<MultipartBody.Part> post_photos );

    @PUT("api/post/{post_id}")
    @Headers({"Accept: application/json"})
    Call<JSONObject> deleteNewsPhoto(@Header("Authorization") String authHeader, @Path("post_id") String postID, @Query("delete_photos[0]") int photoID);

    @PUT("api/user/post/{post_id}")
    @Headers({"Accept: application/json"})
    Call<JSONObject> deleteAppealPhoto(@Header("Authorization") String authHeader, @Path("post_id") String postID, @Query("delete_photos[0]") int photoID);


    @DELETE("api/post/{post_id}")
    Call<ResponseBody> deleteNews(@Header("Authorization") String authHeader,@Path("post_id") int id);
    @DELETE("api/user/post/{post_id}")
    Call<ResponseBody> deleteAppeal(@Header("Authorization") String authHeader,@Path("post_id") int id);


    @GET("api/post")
    Call<ServerListResponse<News>> getNewsList(@Header("Authorization") String authHeader, @Query("page") int page);

    @DELETE("api/user/post/{user_post_id}")
    Call<ResponseBody> deleteUserAppeal(@Header("Authorization") String authHeader, @Path("user_post_id") int id);

    @POST("api/user/post/{user_post_id}/accept")
    Call<ResponseBody> acceptAppealByAdmin(@Header("Authorization") String authHeader, @Path("user_post_id") int id, @Query("comment") String comment);

    @POST("api/user/post/{user_post_id}/confirm")
    Call<ResponseBody> acceptAppealByAuthor(@Header("Authorization") String authHeader, @Path("user_post_id") int id);

    @POST("api/user/post/{user_post_id}/{like}")
    Call<ResponseBody> likeAppeal(@Header("Authorization") String authHeader, @Path("user_post_id") int id, @Path("like") String like);

    @GET("api/user/post")
    Call<ServerListResponse<Appeal>> getAppeals(@Header("Authorization") String authHeader, @Query("mode") String mode, @Query("page") String page);





    @FormUrlEncoded
    @POST("api/bus/event")
    @Headers({"Accept: application/json"})
    Call<JsonObject> addBusEvent(@Header("Authorization") String token,@Field("title") String title,@Field("place")String place,@Field("time")String time);

    @DELETE("api/bus/event/{bus_event_id}")

    Call<JsonObject> deleteBusEvent(@Header("Authorization") String token,@Path("bus_event_id") int id);

    @GET("api/bus/event")
    Call<ServerListResponse<BusJSON>> getBusList(@Header("Authorization") String authHeader);























    @FormUrlEncoded
    @POST
    @Headers({"Accept: application/json","Authorization: Bearer {{token}}"})
    Call<ResponseBody> addFile(@Field("title")File title,@Field("file")File file);

    @GET("api/file/{{file_id}}")
    @Headers({"Accept: application/json","Authorization: Bearer {{token}}"})
    Call<ResponseBody> downloadFile();

    @DELETE("api/file/{{file_id}}")
    @Headers({"Authorization: Bearer {{token}}"})
    Call<ResponseBody> deleteFille();

    @GET("api/file")
    @Headers({"Authorization: Bearer {{token}}"})
    Call<ResponseBody> getFileList();








    @FormUrlEncoded
    @POST("api/mapObject")
    @Headers({"Accept: application/json","Authorization: Bearer {{token}}"})
    Call<ResponseBody> addMapObject(@Field("name")String name,@Field("color") String color,@Field("coords[0]") float coords1,@Field("coords[1]") float coords2, @Field("type") String type,@Field("points")int points);

    @FormUrlEncoded
    @POST("api/mapObject/{{map_object_id}}")
    @Headers({"Accept: application/json","Authorization: Bearer {{token}}"})
    Call<ResponseBody> editMapObject(@Field("name")String name,@Field("color") String color,@Field("coords[0]") float coords1,@Field("coords[1]") float coords2, @Field("type") String type,@Field("points")int points);

    @DELETE("api/mapObject/{{map_object_id}}")
    @Headers({"Authorization: Bearer {{token}}"})
    Call<ResponseBody> deleteMapObject();

    @GET("api/mapObject")
    Call<ServerListResponse<MapObject>> getMapObject();





    @PUT("api/auth/profile")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> editProfile(@Header("Authorization") String token,@Body User user);


    @Headers({"Accept: application/json"})
    @PUT("api/user/{user_id}")
    Call<ResponseBody> editUser(@Header("Authorization") String token, @Path("user_id") int user_id, @Body User user);

    @GET("api/user")
    Call<ServerListResponse<User>> getUsers(@Header("Authorization") String heder);
    @GET("api/user")
    Call<ServerListResponse<User>> getUsers(@Header("Authorization") String heder, @Query("name") String name);


    @GET("api/event")
    Call<ServerListResponse<Event>> getEventList(@Header("Authorization") String authHeader, @Query("page") int id);

    @POST("api/event")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> addEvent(@Header("Authorization") String token,@Query("title") String title, @Query("place")String place,@Query("date") String date,@Query("points") String points);

    @Headers({"Accept: application/json"})
    @PUT("api/event/{event_id}")
    Call<ResponseBody> addEventParticipant(@Header("Authorization") String heder, @Path("event_id") int event_id, @Body Map<String,String> body);

    @Headers({"Accept: application/json"})
    @PUT("api/event/{event_id}")
    Call<ResponseBody> editEvent(@Header("Authorization") String heder, @Path("event_id") int event_id, @Body Event body);

    @Headers({"Accept: application/json"})
    @DELETE("api/event/{event_id}")
    Call<ResponseBody> deleteEvent(@Header("Authorization") String heder, @Path("event_id") int event_id);

    @GET("api/request")
    Call<ServerListResponse<UserRequest>> getUserRequests(@Header("Authorization") String header, @Query("role") int role, @Query("page") int p);

    @POST("api/request")
    @Headers({"Accept: application/json"})
    Call<ServerItemResponse<UserRequest>> addUserRequests(@Header("Authorization") String header, @Body UserRequest message);

    @DELETE("api/request/{id}")
    Call<ResponseBody> deleteUserRequest(@Header("Authorization") String header, @Path("id") String id);


    @GET("api/request/{id}/messages?per_page=1000000")
    Call<ServerListResponse<Message>> getUserRequestMessages(@Header("Authorization") String header, @Path("id") String id);
    @POST("api/request/{id}/messages?per_page=1000000")
    @Headers({"Accept: application/json"})
    Call<ServerListResponse<Message>> sendUserRequestMessage(@Header("Authorization") String header, @Path("id") String id, @Body Message message);
}
