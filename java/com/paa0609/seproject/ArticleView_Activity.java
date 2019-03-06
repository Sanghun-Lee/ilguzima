package com.paa0609.seproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ArticleView_Activity extends AppCompatActivity {

    private AlertDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);

        final Intent intent = getIntent();
        final ArticleObject articleObject = (ArticleObject) intent.getSerializableExtra("MyButtonArticleObject");
        boolean myArticle = articleObject.isMyArticle();


        TextView Title = (TextView) findViewById(R.id.articleTitle);
        final TextView BusNum = (TextView) findViewById(R.id.busNum);
        TextView Object = (TextView) findViewById(R.id.articleObject);
        EditText contents = (EditText) findViewById(R.id.contents);


        Title.setText(articleObject.getTitle());
        BusNum.setText(articleObject.getBusNum());
        BusNum.setTextColor(getResources().getColor(R.color.colorDark));
        Object.setText(articleObject.getObject());
        Object.setTextColor(getResources().getColor(R.color.colorDark));
        contents.setText(articleObject.getContents());

        final Button ModifyButton = (Button) findViewById(R.id.modifyArticleButton);
        final Button deleteButton = (Button) findViewById(R.id.deleteArticleButton);
        final Button completeButton = (Button) findViewById(R.id.completeButton);

        final String PostTime = articleObject.getPostTime();    //////////////

        if(articleObject.isComplete() == true) {
            myArticle = false;
            completeButton.setBackgroundColor(getResources().getColor(R.color.colorWarning));
            Title.setEnabled(false);
            contents.setEnabled(false);
            contents.setBackgroundColor(getResources().getColor(R.color.colorGray));
            Title.setBackgroundColor(getResources().getColor(R.color.colorGray));
        }


        if(myArticle == false) {
            ModifyButton.setEnabled(false);
            deleteButton.setEnabled(false);
            completeButton.setEnabled(false);
        }




        ModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleObject articleObject1 = new ArticleObject();
                articleObject1.setID(articleObject.getID());
                articleObject1.setBusNum(articleObject.getBusNum());
                articleObject1.setTitle(articleObject.getTitle());
                articleObject1.setObject(articleObject.getObject());
                articleObject1.setContents(articleObject.getContents());
                articleObject1.setPostTime(articleObject.getPostTime());
                articleObject1.setPhoneNum(articleObject.getPhoneNum());

                Intent modifyArticleIntent = new Intent(ArticleView_Activity.this, Modify_Activity.class);
                modifyArticleIntent.putExtra("ModityArticle", articleObject1);
                ArticleView_Activity.this.startActivity(modifyArticleIntent);



            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleView_Activity.this);
                dialog = builder.setMessage("정말 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success) {
                                               finish();
                                            }
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                DeleteRequest deleteRequest = new DeleteRequest(articleObject.getPostTime(), articleObject.getID(), responseListener);
                                RequestQueue queue = Volley.newRequestQueue(ArticleView_Activity.this);
                                queue.add(deleteRequest);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder comp = new AlertDialog.Builder(ArticleView_Activity.this);
                comp.setTitle("전달 완료");
                comp.setMessage("받은사람의 전화번호를 입력해 주세요");

                final EditText text = new EditText(ArticleView_Activity.this);
                comp.setView(text);

                comp.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phoneNum = text.getText().toString();
                        articleObject.setComplete(true);

                        finish();
                    }
                });
                comp.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                comp.show();
            }
        });







    }
}
