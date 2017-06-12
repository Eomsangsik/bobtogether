package com.yesjam.bobtogether.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yesjam.bobtogether.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainSecondRecyclerAdapter extends RecyclerView.Adapter<MainSecondRecyclerAdapter.RecyclerViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<secondItem> secondItems;

    public MainSecondRecyclerAdapter(Context context, ArrayList<secondItem> secondItems) {

        this.context = context;
        this.secondItems = secondItems;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = inflater.inflate(R.layout.main_second_recyclerview_item, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {

        Glide.with(context).load(secondItems.get(position).imgUrl)
                .into(recyclerViewHolder.seoncdImage);
        recyclerViewHolder.title.setText(secondItems.get(position).menuName);
        recyclerViewHolder.place.setText("장소 : " + secondItems.get(position).place);
        recyclerViewHolder.time.setText("일시 : " + secondItems.get(position).time);
        recyclerViewHolder.memNum.setText("사람수 : " + secondItems.get(position).memNum);
        recyclerViewHolder.madeByWho.setText("작성자 : " + secondItems.get(position).madeByWho);

    }

    @Override
    public int getItemCount() {
        return secondItems.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView seoncdImage;
        private TextView title;
        private TextView place;
        private TextView time;
        private TextView memNum;
        private TextView madeByWho;

        public RecyclerViewHolder(View view) {
            super(view);

            seoncdImage = (CircleImageView) view.findViewById(R.id.second_iamge);
            title = (TextView) view.findViewById(R.id.second_title);
            place = (TextView) view.findViewById(R.id.second_place);
            time = (TextView) view.findViewById(R.id.second_time);
            memNum = (TextView) view.findViewById(R.id.second_memNum);
            madeByWho = (TextView) view.findViewById(R.id.second_madeByWho);

        }
    }

    public static class secondItem {

        private String imgUrl, place, time, menuName, memNum, madeByWho;

        public secondItem(String imgUrl, String menuName, String place, String time, String memNum, String madeByWho) {

            this.imgUrl = imgUrl;
            this.menuName = menuName;
            this.place = place;
            this.time = time;
            this.memNum = memNum;
            this.madeByWho = madeByWho;

        }
    }
}

