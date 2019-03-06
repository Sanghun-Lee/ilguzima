package com.paa0609.seproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditRequest extends StringRequest {

    final static private String URL = "http://119.201.88.59:80/ilguzima/EditUserInfo.php";

    private Map<String, String> parameters;

    public EditRequest(String userID, String presentPW, String nextPW, String busNum, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("presentPW", presentPW);
        parameters.put("nextPW", nextPW);
        parameters.put("busNum", busNum);

    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
