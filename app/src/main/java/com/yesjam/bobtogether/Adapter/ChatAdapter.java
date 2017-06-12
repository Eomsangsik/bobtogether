package com.yesjam.bobtogether.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yesjam.bobtogether.ChatActivity;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;
import com.yesjam.bobtogether.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ChatActivity.User> messageList;
    private UserDataPreferences userDataPreferences;

    public ChatAdapter(Context mContext, ArrayList<ChatActivity.User> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
        this.userDataPreferences = new UserDataPreferences(mContext);
    }

    @Override
    public int getItemViewType(int position) {

        if (userDataPreferences.getEmail().equals(messageList.get(position).email)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.myitem_chat, viewGroup, false);
            MyItemViewHolder myItemViewHolder = new MyItemViewHolder(view);
            return myItemViewHolder;

        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, viewGroup, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (userDataPreferences.getEmail().equals(messageList.get(position).email)) {
            MyItemViewHolder holder = (MyItemViewHolder) viewHolder;
            holder.myDate.setText(messageList.get(position).date);
            holder.myMessage.setText(messageList.get(position).message);
        } else {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
//        holder.profileImage.setImageResource(messageList.get(i).imageResourceId);
            holder.name.setText(messageList.get(position).userName);
            holder.date.setText(messageList.get(position).date);
            holder.message.setText(messageList.get(position).message);
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        //        private ImageView profileImage;
        private TextView name;
        private TextView date;
        private TextView message;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder {

        private TextView myDate;
        private TextView myMessage;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            myDate = (TextView) itemView.findViewById(R.id.chat_item_my_date);
            myMessage = (TextView) itemView.findViewById(R.id.chat_item_my_message);
        }
    }

}
