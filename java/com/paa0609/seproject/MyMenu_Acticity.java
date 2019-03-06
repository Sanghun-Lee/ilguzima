package com.paa0609.seproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyMenu_Acticity extends AppCompatActivity {

    private LoginObject loginObject;

    private ListView articleListView;
    private ArticleListAdapter adaptor;
    private List<Article> articleList;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_button_layout);

        final Intent intent = getIntent();
        loginObject = (LoginObject) intent.getSerializableExtra("myButtonObject");

        final String userID = loginObject.getID();
        articleListView = (ListView) findViewById(R.id.myarticleList);
        articleList = new ArrayList<Article>();

        new BackgroundTask(userID).execute();

        final ImageButton BackImageButton = (ImageButton) findViewById(R.id.backButton);
        final Button editButton = (Button) findViewById(R.id.editButton);
        final TextView idText = (TextView) findViewById(R.id.myMenuID);
        final Button logoutButton = (Button) findViewById(R.id.logoutButton);

        idText.setText(userID);


        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String > responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {   // UserInfoRequest and UserObject
                            JSONObject jsonResponse = new JSONObject(response);

                            UserObject userObject = new UserObject();
                            userObject.setID(userID);
                            userObject.setName(jsonResponse.getString("NAME"));
                            userObject.setPhoneNum(jsonResponse.getString("TEL"));
                            userObject.setBusNum(jsonResponse.getString("BUSNUM"));

                            Intent editIntent = new Intent(MyMenu_Acticity.this, BusDriver_Edit_Information_Activity.class);
                            editIntent.putExtra("UserObject", userObject);
                            MyMenu_Acticity.this.startActivity(editIntent);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UserInfoRequset editRequset = new UserInfoRequset(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyMenu_Acticity.this);
                queue.add(editRequset);

            }
        });

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                ArticleObject articleObject = new ArticleObject();
                                articleObject.setID(jsonResponse.getString("ID"));
                                articleObject.setPhoneNum(jsonResponse.getString("TEL"));
                                articleObject.setBusNum(jsonResponse.getString("BUSNUM"));
                                articleObject.setContents(jsonResponse.getString("BOARD"));
                                articleObject.setObject(jsonResponse.getString("OBJECT"));
                                articleObject.setPostTime(jsonResponse.getString("POSTTIME"));
                                articleObject.setTitle(jsonResponse.getString("TITLE"));
                                articleObject.setMyArticle(true);

                                Intent viewArticleIntent = new Intent(MyMenu_Acticity.this, ArticleView_Activity.class);
                                viewArticleIntent.putExtra("MyButtonArticleObject", articleObject);
                                MyMenu_Acticity.this.startActivity(viewArticleIntent);
                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyMenu_Acticity.this);
                                dialog = builder.setMessage("글을 읽지 못했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }


                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                ArticleRequest articleRequest = new ArticleRequest((articleList.get(position)).getArticleTime(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyMenu_Acticity.this);
                queue.add(articleRequest);

            }

        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyMenu_Acticity.this);
                dialog = builder.setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences loginInfo = getSharedPreferences("loginInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = loginInfo.edit();
                                editor.remove("ID");
                                editor.remove("PW");
                                editor.commit();


                                Intent logoutIntent = new Intent(MyMenu_Acticity.this, LoginActivity.class);
                                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(logoutIntent);
                                finish();
                                //MyMenu_Acticity.this.startActivity(logoutIntent);
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




    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String userID;

        public BackgroundTask(String userID) {
            this.userID = userID;
        }

        String target; // 접속할 homepage 주소

        @Override
        protected void onPreExecute() {
            target = "http://119.201.88.59:80/ilguzima/MyArticleList.php?userID=" + userID;    // target file과 input value를 넣는다.
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {   // 실질적으로  data를 얻어올 수 있는 부분이 작성된다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream(); /// input stream을 받는다.

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stream에 있는 내용을 buffer에 담는다.
                String temp;

                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim(); // 다 들어간 문자열들 반환


            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {   // result 응답부분 처리
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");  // response에 버스 게시글에 대한 정보가 담길것이다. * 변수이름 *
                int count = 0;
                String BusNum, Object, Title, FindLost, articleTime;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    BusNum = object.getString("BUSNUM");
                    Object = object.getString("OBJECT");
                    Title = object.getString("TITLE");
                    articleTime = object.getString("POSTTIME");
                    articleList.add(new Article(Title, BusNum, Object, articleTime));

                    count++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            adaptor = new ArticleListAdapter(getApplicationContext(), articleList);
            articleListView.setAdapter(adaptor);

        }
    }
}
