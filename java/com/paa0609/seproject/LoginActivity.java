package com.paa0609.seproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.SaveInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final EditText idText = (EditText) findViewById(R.id.IDText);
        final EditText pwText = (EditText) findViewById(R.id.passwordText);

        final Button loginButton = (Button) findViewById(R.id.loginButton);

        final CheckBox StayLogin = (CheckBox) findViewById(R.id.stayLogin);


        TextView registerationButton = (TextView) findViewById(R.id.registerButton);

        SharedPreferences loginInfo = getSharedPreferences("loginInfo", MODE_PRIVATE);
        if(loginInfo.contains("ID") == true)    // 아이디가 있으면 자동로그인
        {
            final String userID = loginInfo.getString("ID", "");
            final String userPW = loginInfo.getString("PW", "");

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            dialog = builder.setMessage("로그인에 성공했습니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();

                            LoginObject loginObject = new LoginObject();
                            loginObject.setID(userID);
                            loginObject.setBusNum(jsonResponse.getString("BUSNUM"));
                            loginObject.setPhoneNum(jsonResponse.getString("TEL"));

                            Intent loginIntent = new Intent(LoginActivity.this, AppMainActivity.class);
                            loginIntent.putExtra("LoginObject", loginObject);
                            LoginActivity.this.startActivity(loginIntent);
                            finish();
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(userID, userPW, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = idText.getText().toString();
                final String userPW = pwText.getText().toString();
                final boolean stayLogin = StayLogin.isChecked();

                if(userID.equals("") || userPW.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("모두 작성해 주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                LoginObject loginObject = new LoginObject();
                                loginObject.setID(userID);
                                loginObject.setBusNum(jsonResponse.getString("BUSNUM"));
                                loginObject.setPhoneNum(jsonResponse.getString("TEL"));

                                if(stayLogin) {
                                    SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("ID", userID).apply();
                                    editor.putString("PW", userPW).apply();
                                }

                                Intent loginIntent = new Intent(LoginActivity.this, AppMainActivity.class);
                                loginIntent.putExtra("LoginObject", loginObject);
                                LoginActivity.this.startActivity(loginIntent);
                                finish();
                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("아이디 혹은 비밀번호를 확인해 주세요.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPW, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });




        registerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterationSelect_Activity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }

    private long lastTimeBackPressed;   // 뒤로갈려면 한번 더 뒤로가기를 누르세요

    @Override
    public  void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());

           //출처: http://codeticker.tistory.com/entry/Android-안드로이드-어플-종료시키기 [CodeTicker]
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러서 종료합니다.", Toast.LENGTH_LONG).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
