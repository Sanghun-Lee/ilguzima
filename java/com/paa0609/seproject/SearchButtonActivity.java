package com.paa0609.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchButtonActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner BusSpinner;
    private Spinner ObjSpinner;
    private AlertDialog dialog;
    ArticleObject articleObject = new ArticleObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_button_layout);



        BusSpinner = (Spinner) findViewById(R.id.searchBusNumSelectSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.busNumber, android.R.layout.simple_spinner_dropdown_item);
        BusSpinner.setAdapter(adapter);

        ObjSpinner = (Spinner) findViewById(R.id.searchObjectSelectSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.objectList, android.R.layout.simple_spinner_dropdown_item);
        ObjSpinner.setAdapter(adapter);


        final RadioButton Find = (RadioButton) findViewById(R.id.findRadioButton);
        final RadioButton Lost = (RadioButton) findViewById(R.id.lostRadioButton);



        Button confimButton = (Button) findViewById(R.id.SearchButton);
        Button cancelButton = (Button) findViewById(R.id.searchCancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confimButton.setOnClickListener(new View.OnClickListener() {


            TextView Title = (TextView) findViewById(R.id.searchTitleTextEdit);


            @Override
            public void onClick(View v) {
                final String busNum = BusSpinner.getSelectedItem().toString();
                final String object = ObjSpinner.getSelectedItem().toString();
                final String title = Title.getText().toString();

                final boolean find = Find.isChecked();
                final boolean lost = Lost.isChecked();

                String LostFind = "Find";
                if(find == false && lost == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchButtonActivity.this);
                    dialog = builder.setMessage("Find / Lost 체크해 주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                else if(find == true) {
                    LostFind = "Find";
                }
                else if(lost == true) {
                    LostFind = "Lost";
                }

                if(busNum.equals("버스번호 선택") && object.equals("물건선택") && title.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchButtonActivity.this);
                    dialog = builder.setMessage("버스번호, 물건종류, 제목중 하나라도 있어야 합니다!")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                articleObject.setBusNum(busNum);
                articleObject.setTitle(title);
                articleObject.setObject(object);
                articleObject.setID(LostFind);


                Intent searchResultIntent = new Intent(SearchButtonActivity.this, SearchResult_Activity.class);
                searchResultIntent.putExtra("SearchResult", articleObject);
                SearchButtonActivity.this.startActivity(searchResultIntent);

            }
        });

    }

}
