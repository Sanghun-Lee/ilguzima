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

public class Modify_Activity extends AppCompatActivity {

    private AlertDialog dialog;
    private Spinner spinner;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_new_article);

        final Intent intent = getIntent();
        ArticleObject articleObject = (ArticleObject) intent.getSerializableExtra("ModityArticle");

        final EditText Title = (EditText) findViewById(R.id.UserTitleText);
        final Spinner busNum = (Spinner) findViewById(R.id.UserbusNumberSelectSpinner);
        final Spinner object = (Spinner) findViewById(R.id.UserobjectSelectSpinner);
        final EditText Contents = (EditText) findViewById(R.id.contents);
        EditText phoneNum = (EditText) findViewById(R.id.UserPhoneNum);

        adapter = ArrayAdapter.createFromResource(this, R.array.busNumber, android.R.layout.simple_spinner_dropdown_item);
        busNum.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.objectList, android.R.layout.simple_spinner_dropdown_item);
        object.setAdapter(adapter);

        phoneNum.setText(articleObject.getPhoneNum());
        phoneNum.setEnabled(false);


        final String userID = articleObject.getID();
        Title.setText(articleObject.getTitle());
        Contents.setText(articleObject.getContents());
        final String posttime = articleObject.getPostTime();

        Button cancel = (Button) findViewById(R.id.userArticleCancelButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Button confirm = (Button) findViewById(R.id.userArticleRegisterButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ArticleTitle = Title.getText().toString();
                final String ArticleBusNum = busNum.getSelectedItem().toString();
                final String ArticleObject = object.getSelectedItem().toString();
                final String ArticleContents = Contents.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(ArticleTitle.equals("")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Modify_Activity.this);
                                dialog = builder.setMessage("제목을 작성해 주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }

                            if(ArticleTitle.length() > 20) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Modify_Activity.this);
                                dialog = builder.setMessage("제목은 20자 이하로 작성해 주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }

                            if(ArticleContents.length() > 200) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Modify_Activity.this);
                                dialog = builder.setMessage("게시글은 200자 이하로 작성해 주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }


                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Modify_Activity.this);
                                dialog = builder.setMessage("게시글이 잘 등록되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Modify_Activity.this);
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

                NewArticleRequest newArticleRequest = new NewArticleRequest(ArticleTitle, ArticleBusNum, ArticleObject, userID, ArticleContents, posttime, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Modify_Activity.this);
                queue.add(newArticleRequest);

            }
        });
    }
}
