package com.paa0609.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NewArticleActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner BusNumspinner;
    private Spinner Objectspinner;

    private UserObject userObject;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_new_article);

        final Intent intent = getIntent();
        userObject = (UserObject) intent.getSerializableExtra("WriteUserObject");

        final String userID = userObject.getID();
        final String phoneNum = userObject.getPhoneNum();
        final String busNum = userObject.getBusNum();



        BusNumspinner = (Spinner) findViewById(R.id.UserbusNumberSelectSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.busNumber, android.R.layout.simple_spinner_dropdown_item);
        BusNumspinner.setAdapter(adapter);

        Objectspinner = (Spinner) findViewById(R.id.UserobjectSelectSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.objectList, android.R.layout.simple_spinner_dropdown_item);
        Objectspinner.setAdapter(adapter);


        final EditText FindLost = (EditText) findViewById(R.id.Lostcategory);
        FindLost.setEnabled(false);

        if(busNum.equals("null")) {
            FindLost.setText("Lost");
        }
        else {
            FindLost.setText("Find");
        }

        EditText PhoneNum = (EditText) findViewById(R.id.UserPhoneNum);
        PhoneNum.setText(phoneNum);

        PhoneNum.setEnabled(false);

        final EditText titleText = findViewById(R.id.UserTitleText);
        final EditText contentsText = findViewById(R.id.contents);

        Button PostButton = (Button) findViewById(R.id.userArticleRegisterButton);

        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ArticleTitle = titleText.getText().toString();
                final String ArticleBusNum = BusNumspinner.getSelectedItem().toString();
                final String ArticleObject = Objectspinner.getSelectedItem().toString();
                final String ArticleFindLost = FindLost.getText().toString();
                final String ArticleContents = contentsText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {   // NewArticleRequest
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(ArticleTitle.equals("")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                                dialog = builder.setMessage("제목을 작성해 주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }

                            if(ArticleTitle.length() > 20) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                                dialog = builder.setMessage("제목은 20자 이하로 작성해 주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }

                            if(ArticleContents.length() > 200) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                                dialog = builder.setMessage("게시글은 200자 이하로 작성해 주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }


                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                                dialog = builder.setMessage("게시글이 잘 등록되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewArticleActivity.this);
                                dialog = builder.setMessage("게시글 등록에 실패했습니다..")
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

                NewArticleRequest newArticleRequest = new NewArticleRequest(ArticleTitle, ArticleFindLost, ArticleBusNum, ArticleObject, userID, ArticleContents, phoneNum, responseListener);
                RequestQueue queue = Volley.newRequestQueue(NewArticleActivity.this);
                queue.add(newArticleRequest);


            }
        });


        Button cancelButton = (Button) findViewById(R.id.userArticleCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });











    }
}
