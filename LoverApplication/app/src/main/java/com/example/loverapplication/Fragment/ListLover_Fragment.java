package com.example.loverapplication.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loverapplication.Activity.AddNewLoverActivity;
import com.example.loverapplication.Activity.DetailLoverActivity;
import com.example.loverapplication.Activity.LoginActivity;
import com.example.loverapplication.Adapter.Adapter_RecyclerView;
import com.example.loverapplication.Model.Lover;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLover_Fragment extends Fragment {
    FloatingActionButton btnAdd;
    RecyclerView recyclerView;
    Adapter_RecyclerView adapter;
    TextView tv_no_result;
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_lover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        refreshLayout = view.findViewById(R.id.refresh_data_listlover);

        updateData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewLoverActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String id_user = preferences.getString("id", "");
        RetrofitClient.managerServices().getLoverWithIdUser(id_user).enqueue(new Callback<List<Lover>>() {
            @Override
            public void onResponse(Call<List<Lover>> call, Response<List<Lover>> response) {
                if (response.isSuccessful()) {
                    adapter = new Adapter_RecyclerView(getContext(), response.body());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Lover>> call, Throwable t) {
                System.out.println(t);
                tv_no_result.setText("Không có dữ liệu");

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void requestLoginAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Phiên đăng nhập của bạn đã hết hạn. Vui lòng đăng nhập lại để tiếp tục sử dụng ứng dụng");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences preferences = getActivity().getSharedPreferences("user-login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("is_login", false);
                editor.commit();

                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}