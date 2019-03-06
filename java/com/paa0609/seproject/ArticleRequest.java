package com.paa0609.seproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ArticleRequest extends StringRequest {
    final static private String URL = "http://119.201.88.59:80/ilguzima/GetAllPost.php";

    private Map<String, String> parameters;

    public ArticleRequest(String articleTime, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("posttime", articleTime);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
