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

public class JoinedRecyclerAdapter extends RecyclerView.Adapter<JoinedRecyclerAdapter.RecyclerViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<joinedItem> joinPeople;

    public JoinedRecyclerAdapter(Context context, ArrayList<joinedItem> joinPeople) {

        this.context = context;
        this.joinPeople = joinPeople;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = inflater.inflate(R.layout.join_people_profile_recyclerview_item, viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {

        Glide.with(context).load(joinPeople.get(position).imgUrl)
                .into(recyclerViewHolder.joinedImage);
        recyclerViewHolder.nichName.setText(joinPeople.get(position).nickName);
        recyclerViewHolder.stateMessage.setText(joinPeople.get(position).stateMessage);

    }

    @Override
    public int getItemCount() {
        return joinPeople.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView joinedImage;
        private TextView nichName;
        private TextView stateMessage;

        public RecyclerViewHolder(View view) {
            super(view);

            joinedImage = (CircleImageView) view.findViewById(R.id.joined_iamge);
            nichName = (TextView) view.findViewById(R.id.joined_nick_name);
            stateMessage = (TextView) view.findViewById(R.id.joined_state_message);

        }
    }

    public static class joinedItem {

        private String imgUrl, nickName, stateMessage;

        public joinedItem(String imgUrl, String nickName, String stateMessage) {

            this.imgUrl = imgUrl;
            this.nickName = nickName;
            this.stateMessage = stateMessage;

        }
    }
}

