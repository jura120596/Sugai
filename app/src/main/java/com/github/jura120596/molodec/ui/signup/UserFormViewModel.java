package com.github.jura120596.molodec.ui.signup;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.jura120596.molodec.R;
import com.github.jura120596.molodec.data.User;
import com.github.jura120596.molodec.retrofit.call.ValidateCallback;
import com.github.jura120596.molodec.retrofit.responses.ValidationResponse;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UserFormViewModel extends ViewModel {

    private MutableLiveData<UserFormState> signUpFormState = new MutableLiveData<>();
    private MutableLiveData<UserFormResult> signupResult = new MutableLiveData<>();
    private MutableLiveData<UserFormResult> userEditResult = new MutableLiveData<>();
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private Context context;
    UserFormViewModel() {
        userData.setValue(new User());
    }

    public LiveData<UserFormState> getSignUpFormState() {
        return signUpFormState;
    }

    public MutableLiveData<UserFormResult> getUserEditResult() {
        return userEditResult;
    }

    LiveData<UserFormResult> getSignupResult() {
        return signupResult;
    }
    public LiveData<User> getUserData() {
        return userData;
    }

    public void sendRequest(Call<ResponseBody> call) {

        call.enqueue(new ValidateCallback<ResponseBody>() {
            @Override
            public void on422(Call<ResponseBody> call, Response<ResponseBody> response, ValidationResponse vr) {
                HashMap<String, ArrayList<String>> map = vr.getErrors();
                UserFormState form = new UserFormState(false);
                for (String key: map.keySet()) {
                    String error = vr.getError(key);
                    if (error != null) form.addError(key, error);
                }
                signUpFormState.setValue(form);
            }

            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    signupResult.setValue(new UserFormResult(userData.getValue()));
                    userEditResult.setValue(new UserFormResult(userData.getValue()));
                }
            }
            @Override
            public Context getContext() {
                return UserFormViewModel.this.context;
            }
        });
    }

    public void profileDataChanged( String name, String second, String last, String address, String password, String password_confirmation) {
        loginDataChanged(null, name, second,last,address,null, null, null, null, password, password_confirmation, null, null);
    }
    public void loginDataChanged(String username, String name, String second, String last, String address, String phone, String card, String points, Boolean curator) {
        loginDataChanged(username, name, second, last, address,phone, card, points, curator, null, null, null, null);
    }
    public void loginDataChanged(String username, String name, String second, String last, String address, String phone, String card, String points, Boolean curator, String p, String p2, Integer did, Integer village_id) {
        UserFormState state = new UserFormState(true);
        if (p != null && !isPasswordValid(p)) {
            state.addError("password", R.string.invalid_password);
        }
        if (p2 != null && !p2.equals(p)) {
            state.addError("password_confirmation", R.string.invalid_password2);
        }
        if (username != null && !isUserNameValid(username)) {
            state.addError("email", R.string.invalid_username);
        }
        if (!isNameValid(name)) {
            state.addError("name", R.string.invalid_name);
        }
        if (!isNameValid(second)) {
            state.addError("second_name", R.string.invalid_second_name);
        }
        if (!isLastValid(last)) {
            state.addError("last_name", R.string.invalid_last_name);
        }
        if (!isLastValid(address)) {
            state.addError("address", R.string.invalid_address);
        }
        if (phone != null && !isPhoneValid(phone)) {
            if (phone.matches("^\\+?7")) state.addError("phone", R.string.invalid_phone);
            if (phone.length() != 10) state.addError("phone", R.string.invalid_phone_length);
        }
        User value = new User();
        value.setEmail(username);
        value.setAddress(address);
        value.setPhone(phone);
        value.setName(name);
        value.setLast_name(last);
        value.setSecond_name(second);
        value.setPassword(p);
        value.setPassword_confirmation(p2);
        value.setDistrict_id(did);
        value.setVillage_id(village_id);
        if(card != null && !card.isEmpty()) value.setCard_id(card);
        else value.setCard_id("");
        try {
            if (points != null) value.setPoints(Integer.parseInt(points));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if(curator != null) value.setCurator(curator);
        userData.setValue(value);
        signUpFormState.setValue(state);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && (password.isEmpty() || password.trim().length() >= 6);
    }// A placeholder password validation check
    private boolean isNameValid(String name) {
        return name ==null|| name.trim().length() > 2;
    }
    private boolean isLastValid(String name) {
        return name == null || name.isEmpty() ||  name.trim().length() > 2;
    }
    private boolean isPhoneValid(String phone) {
        return phone != null && phone.trim().length() == 10;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}