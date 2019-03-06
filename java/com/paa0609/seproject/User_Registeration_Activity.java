package com.paa0609.seproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class User_Registeration_Activity extends AppCompatActivity {

    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registeration);

        final EditText idText = (EditText) findViewById(R.id.IDRegisterationText);
        final EditText pwText = (EditText) findViewById(R.id.PWRegisterationText);
        final EditText pwConfirmText = (EditText) findViewById(R.id.PWRegisterationConfirmText);
        final EditText nameText = (EditText) findViewById(R.id.RegisterNamePlainText);
        final EditText phoneNum = (EditText) findViewById(R.id.PhoneNumText);

        final Button cancel = (Button) findViewById(R.id.cancelButton);
        Button registerationConfirm = (Button) findViewById(R.id.userRegisterComfirmButton);
        final Button idDupCheckButton = (Button) findViewById(R.id.IDDupCheckButton);

        idDupCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                if(validate) {
                    return;
                }
                if(userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                    dialog = builder.setMessage("아이디는 빈 칸 일수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                if(userID.length() > 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                    dialog = builder.setMessage("아이디는 20자 이하여야 됩니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디 입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate =true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                idDupCheckButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디 입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(User_Registeration_Activity.this);
                queue.add(validateRequest);

            }
        });

        registerationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String userPW = pwText.getText().toString();
                String confirmPW = pwConfirmText.getText().toString();
                String userName = nameText.getText().toString();
                String UserPhoneNum = phoneNum.getText().toString();


                if(!validate)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                    dialog = builder.setMessage("아이디 중복확인먼저 해주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 하나라도 빈부분이 있으면 오류 출력
                if(userID.equals("") || userPW.equals("") || confirmPW.equals("") || userName.equals("") || UserPhoneNum.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }


                if(!userPW.equals(confirmPW))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                    dialog = builder.setMessage("두 비밀번호가 일치하지 않습니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(userPW.length() < 8 || userPW.length() > 20)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                    dialog = builder.setMessage("비밀번호는 8자이상, 20자 이하여야 합니다.")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                                dialog = builder.setMessage("회원가입이 완료되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(User_Registeration_Activity.this);
                                builder.setMessage("회원가입이 실패되었습니다.")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UserRegisterRequest userRegisterRequest = new UserRegisterRequest(userID, userPW, userName, UserPhoneNum, responseListener);
                RequestQueue queue = Volley.newRequestQueue(User_Registeration_Activity.this);
                queue.add(userRegisterRequest);

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onStop() {
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}
