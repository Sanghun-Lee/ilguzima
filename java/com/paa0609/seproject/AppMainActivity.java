package com.paa0609.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppMainActivity extends AppCompatActivity {


//    private ListView articleListView;
//    private ArticleListAdapter adaptor;
//    private List<Article> articleList;

    private LoginObject loginObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_layout);

        final Intent intent = getIntent();
        loginObject = (LoginObject) intent.getSerializableExtra("LoginObject");

        final String userID = loginObject.getID();
        String busNum = loginObject.getBusNum();

        final Button findButton = (Button) findViewById(R.id.FindButton);
        final Button lostButton = (Button) findViewById(R.id.LostButton);
        final Button myButton = (Button) findViewById(R.id.MyButton);
        final LinearLayout find = (LinearLayout) findViewById(R.id.listitem);
        final ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        final ImageButton newArticleButton = (ImageButton) findViewById(R.id.newText);


        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find.setVisibility(View.GONE);
                findButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                lostButton.setBackgroundColor(getResources().getColor(R.color.colorWrite));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorWrite));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new find());
                fragmentTransaction.commit();
            }
        });

        lostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find.setVisibility(View.GONE);
                findButton.setBackgroundColor(getResources().getColor(R.color.colorWrite));
                lostButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                myButton.setBackgroundColor(getResources().getColor(R.color.colorWrite));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new LostFragment());
                fragmentTransaction.commit();
            }
        });

        newArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserObject userObject = new UserObject();
                userObject.setID(userID);
                userObject.setPhoneNum(loginObject.getPhoneNum());
                userObject.setBusNum(loginObject.getBusNum());
                userObject.setName("null");

                Intent writeIntent = new Intent(AppMainActivity.this, NewArticleActivity.class);
                writeIntent.putExtra("WriteUserObject", userObject);
                AppMainActivity.this.startActivity(writeIntent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(AppMainActivity.this, SearchButtonActivity.class);
                AppMainActivity.this.startActivity(searchIntent);
            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginObject = (LoginObject)  intent.getSerializableExtra("LoginObject");

                Intent myButtonIntent = new Intent(AppMainActivity.this, MyMenu_Acticity.class);
                myButtonIntent.putExtra("myButtonObject", loginObject);
                AppMainActivity.this.startActivity(myButtonIntent);
            }
        });
    }

    private long lastTimeBackPressed;   // 뒤로갈려면 한번 더 뒤로가기를 누르세요

    @Override
    public  void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
        {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러서 종료합니다.", Toast.LENGTH_LONG).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
