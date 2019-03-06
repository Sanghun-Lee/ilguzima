package com.paa0609.seproject;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
// 아이디 중복검사하는 클래스

    final static private String URL = "http://119.201.88.59:80/ilguzima/UserValidate.php";  // 우리 서버 URL의 PHP파일와 매칭시킨다.
    // ex "http://OurSEserver.com/Register.php"
    private Map<String, String> parameters;

    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
