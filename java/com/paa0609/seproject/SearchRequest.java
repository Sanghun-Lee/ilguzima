package com.paa0609.seproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest {

    final static private String AllSearch = "http://119.201.88.59:80/ilguzima/SearchPost.php";

    private Map<String, String> parameters;

    public SearchRequest(String LostFind, Response.Listener<String> listener) {
        super(Method.POST, AllSearch, listener, null);
        parameters = new HashMap<>();
        parameters.put("lostfind", LostFind);

    }



    @Override
    public Map<String, String> getParams() { return parameters; }
}
