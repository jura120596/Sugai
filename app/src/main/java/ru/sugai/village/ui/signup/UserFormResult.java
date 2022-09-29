package ru.sugai.village.ui.signup;

import androidx.annotation.Nullable;

import ru.sugai.village.data.User;

/**
 * Authentication result : success (user details) or error message.
 */
public class UserFormResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    UserFormResult(@Nullable Integer error) {
        this.error = error;
    }

    UserFormResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    public User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}