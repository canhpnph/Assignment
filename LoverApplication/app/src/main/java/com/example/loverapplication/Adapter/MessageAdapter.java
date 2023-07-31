package com.example.loverapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.loverapplication.Activity.ChattingActivity;
import com.example.loverapplication.Model.MessageModel;
import com.example.loverapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends ArrayAdapter<MessageModel> {

    String API_image = "http://192.168.1.4:3000/uploads/";

    public MessageAdapter(Context context, int resource, List<MessageModel> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageModel message = getItem(position);

        if (TextUtils.isEmpty(message.getMessage())) {

            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_connected, parent, false);

            TextView messageText = convertView.findViewById(R.id.message_body);

            Log.i(ChattingActivity.TAG, "getView: is empty ");
            String userConnected = message.getUsername();
            messageText.setText(userConnected);

        } else if (message.getId().equals(ChattingActivity.id_message)) {
            Log.i(ChattingActivity.TAG, "getView: " + message.getId() + " " + ChattingActivity.id_message);

            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);
            TextView messageText = convertView.findViewById(R.id.message_body);
            messageText.setText(message.getMessage());

        } else {
            Log.i(ChattingActivity.TAG, "getView: is not empty");

            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.their_message, parent, false);

            TextView messageText = convertView.findViewById(R.id.message_body);
            TextView usernameText = (TextView) convertView.findViewById(R.id.name);
            CircleImageView avatar_chat = convertView.findViewById(R.id.avatar_chat);

//            Glide.with(getContext())
//                    .load(API_image + message.getId_sender().getImage())
//                    .into(avatar_chat);

            messageText.setVisibility(View.VISIBLE);
            usernameText.setVisibility(View.VISIBLE);

            messageText.setText(message.getMessage());
            usernameText.setText(message.getUsername());
        }

        return convertView;
    }
}
