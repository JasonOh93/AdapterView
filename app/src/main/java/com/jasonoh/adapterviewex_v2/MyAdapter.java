package com.jasonoh.adapterviewex_v2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    ArrayList<Member> members;
    LayoutInflater inflater;

    //생성자 메소드 만들기
    public MyAdapter( ArrayList<Member> members, LayoutInflater inflater ){

        this.members = members;
        this.inflater = inflater;

    }//MyAdapter 생성자 메소드

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) convertView = inflater.inflate(R.layout.listview_item, null);

        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_nation = convertView.findViewById(R.id.tv_nation);
        TextView tv_todayDate = convertView.findViewById(R.id.tv_todayDate);
        ImageView iv_nation = convertView.findViewById(R.id.iv_nation);
        ImageView iv_mainImg = convertView.findViewById(R.id.iv_mainImg);

        tv_name.setText( members.get(position).name );
        tv_nation.setText( members.get(position).nation );
        tv_todayDate.setText( members.get(position).todayDate );
        iv_nation.setImageResource( members.get(position).nationId );
        iv_mainImg.setImageResource( members.get(position).mainImageId );

        return convertView;
    }
}
