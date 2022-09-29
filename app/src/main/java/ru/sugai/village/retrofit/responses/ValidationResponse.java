package ru.sugai.village.retrofit.responses;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidationResponse {
    String message;
    HashMap<String, ArrayList<String>> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, ArrayList<String>> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, ArrayList<String>> errors) {
        this.errors = errors;
    }
    public String getError(String field) {
        if (!errors.containsKey(field)) return  null;
        return errors.get(field).get(0);
    }
    public void addError(String field, String error) {
        if (errors == null) errors = new HashMap<>();
        else if (errors.containsKey(field)) {
            ArrayList<String> errors = this.errors.get(field);
            errors.add(error);
            this.errors.put(field, errors);
            return;
        }
        ArrayList<String> errors = new ArrayList<>();
        errors.add(error);
        this.errors.put(field, errors);
        return;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
