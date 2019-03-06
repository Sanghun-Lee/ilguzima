package com.paa0609.seproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteIDRequest extends StringRequest {
    final static private String URL = "http://119.201.88.59:80/ilguzima/DeleteID.php";

    private Map<String, String> parameters;

    public DeleteIDRequest(String userID, String userPW, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userPW", userPW);
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}