package com.github.jura120596.molodec.data.database;

import com.github.jura120596.molodec.data.Appeal;
import com.github.jura120596.molodec.data.BusJSON;
import com.github.jura120596.molodec.data.Event;
import com.github.jura120596.molodec.data.History;
import com.github.jura120596.molodec.data.News;
import com.github.jura120596.molodec.data.User;
import com.github.jura120596.molodec.data.UserRequest;
import com.github.jura120596.molodec.data.UserRequestType;

import java.util.ArrayList;
import java.util.List;

public class DataBASE {

    private DataBASE(){}
    public static final List<Appeal> APPEALS_LIST= new ArrayList<>();


    public static User user = new User();

    public static List<BusJSON> BUS_JSON_LIST = new ArrayList<>();
    public static List<User> USERS_LIST = new ArrayList<>();

    public static List<News> NEWS_JSON_LIST = new ArrayList<>();
    public static List<History> HISTORY_JSON_LIST = new ArrayList<>();
    public static List<Event> EVENT_JSON_LIST = new ArrayList<>();
    public static List<UserRequestType> REQUEST_TYPEJSON_LIST = new ArrayList<>();
    public static List<UserRequestType> REQUEST_TYPEJSON_byID_LIST = new ArrayList<>();
    public static List<UserRequest> REQUEST_LIB_LIST = new ArrayList<>();
    public static List<UserRequest> REQUEST_ADMIN_LIST = new ArrayList<>();
    public static  String token = "";

}
