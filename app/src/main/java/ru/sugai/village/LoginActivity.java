package ru.sugai.village;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.sugai.village.CONST.CONST;
import ru.sugai.village.data.User;
import ru.sugai.village.data.database.DataBASE;
import ru.sugai.village.retrofit.Retrofit;
import ru.sugai.village.retrofit.responses.ObjectResponse;
import ru.sugai.village.ui.signup.SignUpActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {

    public static final String LOGIN_PREFS = "login";
    EditText login, password;
    TextView signup, reset, title;
    Button button;
    boolean loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loginMode = true;
        password = findViewById(R.id.etv_login_password);
        title = findViewById(R.id.login_title);
        login = findViewById(R.id.etv_login_login);
        signup = findViewById(R.id.signup);
        reset = findViewById(R.id.reset);
//        login.setText("a@mail.ru");
//        password.setText("admin2");
        button = findViewById(R.id.bt_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                if(loginMode)  getToken();
                else resetPassword();
            }
        });
        reset.setOnClickListener((view) -> {
            loginMode = !loginMode;
            password.setText("");
            if (loginMode) {
                button.setText("Войти");
                reset.setText("Забыли пароль?");
                password.setVisibility(View.VISIBLE);
            } else {
                reset.setText("Войти с паролем");
                button.setText("Восстановить");
                password.setVisibility(View.INVISIBLE);
            }

        });
        signup.setOnClickListener((view) -> {
            startActivityForResult(new Intent(getBaseContext(), SignUpActivity.class), Activity.RESULT_OK);
        });
        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (loginMode) getToken();
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = userToken(this);
        if (loginMode && token!=null && !token.isEmpty()) {
            signup.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            password.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.INVISIBLE);
            title.setText("Загрузка....");
            getProfile(true);
        }
    }

    private  void getToken() {
        Call<JsonObject> call = Retrofit.getInstance().getApi().login(login.getText().toString(), password.getText().toString());
        call.enqueue(new LoginCallback<JsonObject>() {
            @Override
            public void onSuccess(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String token = jsonObject.getJSONObject("data").getString("access_token");
                    saveUserToken(getBaseContext(), DataBASE.token = token);
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    getProfile();
                }
            }
        });
    }

    private  void resetPassword() {
        Call<ResponseBody> call = Retrofit.getInstance().getApi().resetPassword(login.getText().toString());
        call.enqueue(new LoginCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getBaseContext(), "Пароль для входа был отправлен на вашу почту", Toast.LENGTH_LONG).show();
                }
                button.setEnabled(true);
            }
        });
    }
    public User getProfile() {
        return getProfile(false);
    }
    public User getProfile(boolean resume) {
        String token = userToken(getBaseContext());
        if (token == null) return null;
        Call<ObjectResponse<User>> getProfileData = Retrofit.getInstance().getApi().getProfile("Bearer " + token);
        button.setEnabled(false);
        getProfileData.enqueue(new Callback<ObjectResponse<User>>() {
            @Override
            public void onResponse(Call<ObjectResponse<User>> call, Response<ObjectResponse<User>> response) {
                if (response.isSuccessful()) {
                    DataBASE.user = response.body().getData();
                    Log.d(CONST.SERVER_LOG, "USERS " + DataBASE.user);
                    startActivity(new Intent(getApplicationContext(), ru.sugai.village.MainActivity.class));
                }
                button.setEnabled(true);
                finishRequest();
            }

            @Override
            public void onFailure(Call<ObjectResponse<User>> call, Throwable t) {
                if (!resume) Toast.makeText(LoginActivity.this, "Не удалось загрузить данные о профиле", Toast.LENGTH_LONG).show();
                Log.d(CONST.SERVER_LOG, "TOKEN " + token);
                button.setEnabled(true);
                finishRequest();
                t.printStackTrace();
            }
        });
        return  DataBASE.user;
    }
    public static String userToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.LOGIN_PREFS, 0);
        return sharedPreferences.getString(CONST.USER_TOKEN, "");
    }

    public static void saveUserToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.LOGIN_PREFS, 0);
        if (token != null){
            sharedPreferences.edit().putString(CONST.USER_TOKEN, token).commit();
            Log.d(CONST.SERVER_LOG, "Токен cохранён: " + token);
        }
        else{
            sharedPreferences.edit().remove(CONST.USER_TOKEN).commit();
            Log.d(CONST.SERVER_LOG, "Токен удален: ");
        }
    }

    abstract class LoginCallback<T> implements Callback<T> {
        abstract public void onSuccess(Call<T> call, Response<T> response);
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.code() >= 500) {
                Toast.makeText(LoginActivity.this, "Ошибка сервера", Toast.LENGTH_LONG).show();
                button.setEnabled(true);
                return;
            } else if (response.code() >= 400) {
                Toast.makeText(LoginActivity.this,
                        loginMode
                                ? "Неправильный логин или пароль"
                                : "Такого пользователя не существует",
                        Toast.LENGTH_LONG).show();
                button.setEnabled(true);
                return;
            }
            this.onSuccess(call, response);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "Не могу связаться с сервером. Проверьте включен ли у вас инетернет.", Toast.LENGTH_LONG).show();
            Log.d(CONST.SERVER_LOG, "ERROR: " + t.getMessage());
            button.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String email = data.getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            login.setText(email);
        }
    }
    public void finishRequest() {
        signup.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        reset.setVisibility(View.VISIBLE);
        title.setText("Авторизация");
    }
}