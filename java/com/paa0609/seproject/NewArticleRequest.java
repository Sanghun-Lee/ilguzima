package com.paa0609.seproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewArticleRequest extends StringRequest {

    final static private String URL = "http://119.201.88.59:80/ilguzima/InsertPost.php";
    final static private String MODIFYURL = "http://119.201.88.59:80/ilguzima/ModifyPost.php";

    private Map<String, String> parameters;

    public NewArticleRequest(String title, String LostFind, String busNum, String Object, String userID, String board, String tel, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("title", title);
        parameters.put("lostFind", LostFind);
        parameters.put("busNum", busNum);
        parameters.put("board", board);
        parameters.put("Object", Object);
        parameters.put("userID", userID);
        parameters.put("tel", tel);
    }

    public NewArticleRequest(String title, String busNum, String Object, String userID, String board, String posttime, Response.Listener<String> listener)
    {
        super(Method.POST, MODIFYURL, listener, null);
        parameters = new HashMap<>();

        parameters.put("title", title);
        parameters.put("busnum", busNum);
        parameters.put("board", board);
        parameters.put("object", Object);
        parameters.put("userID", userID);
        parameters.put("posttime", posttime);
    }



    @Override
    public Map<String, String> getParams() { return parameters; }
}
