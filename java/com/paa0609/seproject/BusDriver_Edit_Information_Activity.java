package com.paa0609.seproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class BusDriver_Edit_Information_Activity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;

    private UserObject userObject;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busdriver_edit_information);

        final Intent intent = getIntent();
        userObject = (UserObject) intent.getSerializableExtra("UserObject");

        spinner = (Spinner) findViewById(R.id.bus_edit_busNum_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.busNumber, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final String userID = userObject.getID();
        final String Name = userObject.getName();
        final String phoneNum = userObject.getPhoneNum();
        final String busNum = userObject.getBusNum();

        final Button cancelButton = (Button) findViewById(R.id.bus_edit_cancel_button);

        final TextView editID = (TextView) findViewById(R.id.bus_edit_id);
        final TextView editName = (TextView) findViewById(R.id.bus_edit_name);
        final TextView editPhoneNum = (TextView) findViewById(R.id.bus_edit_phoneNum);

        final EditText presentPW = (EditText) findViewById(R.id.bus_edit_prsent_PW_editText);
        final EditText nextPW = (EditText) findViewById(R.id.bus_edit_next_PW_editText);
        final EditText nextConfirmPW = (EditText) findViewById(R.id.bus_edit_confirm_PW_editText);

        if(busNum.equals("null"))
            spinner.setEnabled(false);

        editID.setText(userID);
        editName.setText(Name);
        editPhoneNum.setText(phoneNum);

        final Button confirmButton = (Button) findViewById(R.id.bus_edit_confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String befPW = presentPW.getText().toString();
                String aftPW = nextPW.getText().toString();
                String confirmPW = nextConfirmPW.getText().toString();

                if(busNum.equals("null") == false) {
                    String busNum = spinner.getSelectedItem().toString();
                }

                if(aftPW.length() < 8 || aftPW.length() > 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                    dialog = builder.setMessage("새 비밀번호는 8자 이상, 20자 이하여야 합니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(!(aftPW.equals(confirmPW))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                    dialog = builder.setMessage("새 비밀번호가 일치하지 않습니다.")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                                dialog = builder.setMessage("수정되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                                dialog = builder.setMessage("기존 비밀번호가 일치하지 않거나 오류로 인해 수정되지 않았습니다..")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                EditRequest editRequest = new EditRequest(userID, befPW, aftPW, busNum, responseListener);
                RequestQueue queue = Volley.newRequestQueue(BusDriver_Edit_Information_Activity.this);
                queue.add(editRequest);


            }
        });

        final Button Withdrawal = (Button) findViewById(R.id.withdrawal);

        Withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder comp = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                comp.setTitle("회원 탈퇴");
                comp.setMessage("현재 비밀번호를 입력해 주세요");

                final EditText text = new EditText(BusDriver_Edit_Information_Activity.this);
                text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                comp.setView(text);

                comp.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String userPW = text.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if(success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                                        builder.setMessage("회원탈퇴가 정상적으로 완료되었습니다.")
                                                .setPositiveButton("확인", null)
                                                .create()
                                                .show();

                                        Intent exitIntent = new Intent(BusDriver_Edit_Information_Activity.this, LoginActivity.class);
                                        exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(exitIntent);
                                        finish();

                                    }

                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(BusDriver_Edit_Information_Activity.this);
                                        builder.setMessage("비밀번호가 일치하지 않거나, 회원탈퇴에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        DeleteIDRequest deleteIDRequest = new DeleteIDRequest(userID, userPW, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(BusDriver_Edit_Information_Activity.this);
                        queue.add(deleteIDRequest);

                    }
                });
                comp.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                comp.create().show();
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
