package com.example.ub;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class RankingDataAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> userList;

    public RankingDataAdapter(Context context, int layout, List<User> userList) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView tvHoTen , tvJob, tvScore;
        ImageView avatar;
        RelativeLayout relativeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.tvHoTen = (TextView) convertView.findViewById(R.id.txtName);
            holder.tvJob   = (TextView) convertView.findViewById(R.id.txtJob);
            holder.tvScore = (TextView) convertView.findViewById(R.id.txtScore);
            holder.avatar  = (ImageView) convertView.findViewById(R.id.avatar);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rankingLayout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        User user = userList.get(position);
        holder.tvHoTen.setText(user.getName());
        holder.tvJob.setText(user.getJob());
        holder.tvScore.setText(String.valueOf(user.getScore()));
        if(position%2 == 0){
            holder.relativeLayout.setBackgroundColor(Color.rgb(152,218,106));
            Log.d("AA","123");

        }
        if(user.getName().equals("Hien"))
        {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(111,111,111 ,111);
            Log.d("AA","222");
            holder.relativeLayout.setLayoutParams(params);
            

        }
        return convertView;
    }
    public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
