package com.paa0609.seproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ArticleListAdapter  extends BaseAdapter {

    private Context context;
    private List<Article> articleList;

    public ArticleListAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }


    @Override
    public int getCount() {
        return articleList.size()   ;
    }

    @Override
    public Object getItem(int i) {
        return articleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.articlelist, null);
        TextView articleText = (TextView) v.findViewById(R.id.articleList_Title);
        TextView busNumText = (TextView) v.findViewById(R.id.articleList_busNum);
        TextView objectText = (TextView) v.findViewById(R.id.articleList_object);

        articleText.setText(articleList.get(i).getTitle());
        busNumText.setText(articleList.get(i).getBusNum());
        objectText.setText(articleList.get(i).getObject());

        v.setTag(articleList.get(i).getTitle());
        return v;
    }
}
