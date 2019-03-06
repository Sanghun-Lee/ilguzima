package com.paa0609.seproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LostFragment newInstance(String param1, String param2) {
        LostFragment fragment = new LostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ListView articleListView;
    private ArticleListAdapter adapter;
    private List<Article> articleList;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        articleListView = (ListView) getView().findViewById(R.id.lostArticleListView);
        articleList = new ArrayList<Article>();

        new BackgroundTask().execute();

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

                                Intent viewArticleIntent = new Intent(getActivity(), ArticleView_Activity.class);
                                viewArticleIntent.putExtra("MyButtonArticleObject", articleObject);
                                LostFragment.this.startActivity(viewArticleIntent);
                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(articleRequest);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {

        String target; // 접속할 homepage 주소

        @Override
        protected void onPreExecute() {
            target = "http://119.201.88.59:80/ilguzima/LostList.php";    // target file과 input value를 넣는다.
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
            adapter = new ArticleListAdapter(getContext().getApplicationContext(), articleList);
            articleListView.setAdapter(adapter);
        }
    }
}
