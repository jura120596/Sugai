package ru.sugai.village.data.database;

import ru.sugai.village.data.Appeal;
import ru.sugai.village.data.BusJSON;
import ru.sugai.village.data.Event;
import ru.sugai.village.data.History;
import ru.sugai.village.data.News;
import ru.sugai.village.data.User;
import ru.sugai.village.data.UserRequest;
import ru.sugai.village.data.UserRequestType;

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
