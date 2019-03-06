package com.paa0609.seproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://119.201.88.59:80/ilguzima/Login.php";

    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPasswd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPasswd", userPasswd);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
