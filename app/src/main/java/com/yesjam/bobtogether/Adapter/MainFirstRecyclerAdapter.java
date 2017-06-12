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

public class MainFirstRecyclerAdapter extends RecyclerView.Adapter<MainFirstRecyclerAdapter.RecyclerViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<firstItem> firstItems;

    public MainFirstRecyclerAdapter(Context context, ArrayList<firstItem> firstItems) {

        this.context = context;
        this.firstItems = firstItems;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = inflater.inflate(R.layout.main_first_recyclerview_item, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {

        Glide.with(context).load(firstItems.get(position).imgUrl)
                .into(recyclerViewHolder.firstImage);
        recyclerViewHolder.title.setText(firstItems.get(position).title);
        recyclerViewHolder.place.setText("장소 : " + firstItems.get(position).place);
        recyclerViewHolder.pNum.setText("사람수 : " + firstItems.get(position).pNum);
        recyclerViewHolder.time.setText("일시 : " + firstItems.get(position).time);

    }

    @Override
    public int getItemCount() {
        return firstItems.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView firstImage;
        private TextView title;
        private TextView place;
        private TextView pNum;
        private TextView time;

        public RecyclerViewHolder(View view) {
            super(view);

            firstImage = (CircleImageView) view.findViewById(R.id.first_iamge);
            title = (TextView) view.findViewById(R.id.first_item_title);
            place = (TextView) view.findViewById(R.id.first_item_place);
            pNum = (TextView) view.findViewById(R.id.first_item_pNum);
            time = (TextView) view.findViewById(R.id.first_item_time);


        }
    }

    public static class firstItem {

        private String imgUrl;
        private String title;
        private String place;
        private String pNum;
        private String time;

        public firstItem(String imgUrl, String title, String place, String pNum, String time) {

            this.imgUrl = imgUrl;
            this.title = title;
            this.place = place;
            this.pNum = pNum;
            this.time = time;

        }
    }
}

