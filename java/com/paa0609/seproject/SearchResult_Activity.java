package com.paa0609.seproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchResult_Activity extends AppCompatActivity {

    ArticleObject articleObject;
    private ListView articleListView;
    private ArticleListAdapter adaptor;
    private List<Article> articleList;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);

        final Intent intent = getIntent();
        articleObject = (ArticleObject) intent.getSerializableExtra("SearchResult");

        ImageButton backButton = (ImageButton) findViewById(R.id.searchResultBackButton);
        TextView findLost = (TextView) findViewById(R.id.searchResultTextView);

        TextView TextBusNum = (TextView) findViewById(R.id.searching_busNum);
        TextView TextObject = (TextView) findViewById(R.id.searching_object);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        articleListView = (ListView) findViewById(R.id.searchResultListView);
        articleList = new ArrayList<Article>();


        String busNum = articleObject.getBusNum();
        String object = articleObject.getObject();
        String LostFind = articleObject.getID();
        String Title = articleObject.getTitle();

        boolean isSearchBus = false, isSearchObject = false, isSearchTitle = false;

        if(busNum.equals("버스번호 선택") == false)
        {
            TextBusNum.setText(busNum);
            isSearchBus = true;
        }
        if(object.equals("물건선택") == false)
        {
            TextObject.setText(object);
            isSearchObject = true;
        }
        if(Title.equals("") == false) {
            isSearchTitle = true;
        }

        new BackgroundTask(LostFind, busNum, object, Title, isSearchBus, isSearchObject, isSearchTitle).execute();




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
                                articleObject.setMyArticle(false);

                                Intent viewArticleIntent = new Intent(SearchResult_Activity.this, ArticleView_Activity.class);
                                viewArticleIntent.putExtra("MyButtonArticleObject", articleObject);
                                SearchResult_Activity.this.startActivity(viewArticleIntent);
                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResult_Activity.this);
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
                RequestQueue queue = Volley.newRequestQueue(SearchResult_Activity.this);
                queue.add(articleRequest);

            }

        });

    }

/*    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String lostfind;
        String busNum;
        String object;
        String Title;
        boolean isSaerchBus, isSearchObject;

        public BackgroundTask(String lostFind, String busNum, String object, String title, boolean isSaerchBus, boolean isSearchObject) {
            lostfind = lostFind;
            this.busNum = busNum;
            this.object = object;
            Title = title;
            this.isSaerchBus = isSaerchBus;
            this.isSearchObject = isSearchObject;
        }
        String target; // 접속할 homepage 주소

        @Override
        protected void onPreExecute() {
            target = "http://119.201.88.59:80/ilguzima/SearchPost.php";    // target file과 input value를 넣는다.
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
                articleList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");  // response에 버스 게시글에 대한 정보가 담길것이다. * 변수이름 *
                int count = 0;
                String BusNum, Object, Title, postTime;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    BusNum = object.getString("BUSNUM");
                    Object = object.getString("OBJECT");
                    Title = object.getString("TITLE");
                    postTime = object.getString("POSTTIME");
                    Article article = new Article(Title, BusNum, Object, postTime);
                    articleList.add(article);

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
*/


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String lostfind;
        String busNum;
        String object;
        String Title;
        boolean isSaerchBus, isSearchObject, isSearchTitle;
        String target; // 접속할 homepage 주소

        public BackgroundTask(String lostFind, String busNum, String object, String title, boolean isSaerchBus, boolean isSearchObject, boolean isSearchTitle) {
            lostfind = lostFind;
            this.busNum = busNum;
            this.object = object;
            Title = title;
            this.isSaerchBus = isSaerchBus;
            this.isSearchObject = isSearchObject;
            this.isSearchTitle = isSearchTitle;
        }

        @Override
        protected void onPreExecute() {
            target = "http://119.201.88.59:80/ilguzima/SearchPostGet.php?lostfind=" + lostfind;    // target file과 input value를 넣는다.
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
                articleList.clear();
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
                    boolean isTrue = !( (isSaerchBus && ( !busNum.equals(BusNum) )) || (isSearchObject && ( !this.object.equals(Object) )) || (isSearchTitle && ( !this.Title.equals(Title) ))  );
                    if( isTrue ) {
                        articleList.add(new Article(Title, BusNum, Object, articleTime));

                    }
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
