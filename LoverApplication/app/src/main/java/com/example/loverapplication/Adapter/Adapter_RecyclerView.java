package com.example.loverapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loverapplication.Activity.DetailLoverActivity;
import com.example.loverapplication.Model.Lover;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_RecyclerView extends RecyclerView.Adapter<Adapter_RecyclerView.ViewHolder> {
    Context context;
    List<Lover> list ;
    String API_image = "http://192.168.1.4:3000/uploads/";

    public Adapter_RecyclerView(Context context, List<Lover> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView img_avatar, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_tenNY);
            img_avatar = itemView.findViewById(R.id.img_avt);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.layout_item1_lover, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_RecyclerView.ViewHolder holder, int position) {
        Lover model = list.get(position);

        Glide.with(context)
                .load(API_image + model.getImage())
                .into(holder.img_avatar);
        holder.tv_name.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailLoverActivity.class);
                intent.putExtra("model", model);
                view.getContext().startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xoá ?");
                builder.setMessage("Bạn có chắc muốn xoá không?");

                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteModel(model.get_id());
                        list.remove(model);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void deleteModel(String id) {
        RetrofitClient.managerServices().deleteLover(id).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    Toast.makeText(context.getApplicationContext(), "Xoá thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

}
