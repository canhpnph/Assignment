package com.example.loverapplication.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loverapplication.Activity.DetailLoverActivity;
import com.example.loverapplication.Activity.InfoUserActivity;
import com.example.loverapplication.Activity.LoginActivity;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Profile_Fragment extends Fragment {

    String API_image = "http://192.168.1.4:3000/uploads/";

    EditText edt_old_pass, edt_new_pass, edt_renew_pass;
    ImageView img_showpass_old, img_showpass_new, img_showpass_renew;
    User model;
    TextView tv_fullname;
    CircleImageView img;
    private boolean password_old_Showing = false;
    private boolean pass_new_showing = false;
    private boolean pass_renew_showing = false;
    boolean checkLogout = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatButton btnLogout = view.findViewById(R.id.btnLogout);
        AppCompatButton btnInfoUser = view.findViewById(R.id.btnInfoProfile);
        AppCompatButton btnChangePassword = view.findViewById(R.id.btnChangePassword);
        tv_fullname = view.findViewById(R.id.tv_fullname_user);
        img = view.findViewById(R.id.img_avt_user);

        SharedPreferences preferences = getActivity().getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RetrofitClient.managerServices().profile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    model = response.body();
                    tv_fullname.setText(model.getFullname());

                    Glide.with(getContext())
                            .load(API_image + model.getImage())
                            .into(img);

                    if (model.getFullname().isEmpty()) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Thông báo");
                                builder.setMessage("Bạn vui lòng cập nhật thông tin cá nhân để có thể tiếp tục sử dụng ứng dụng");

                                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(getActivity(), InfoUserActivity.class));
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }
                        };

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(runnable, 1000);
                    }
                } else {
                    requestLoginAgain();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("get profile response: " + t);

            }
        });

        btnInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InfoUserActivity.class));
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_change_password);
                dialog.show();

                edt_old_pass = dialog.findViewById(R.id.edt_old_password);
                edt_new_pass = dialog.findViewById(R.id.edt_new_password);
                edt_renew_pass = dialog.findViewById(R.id.edt_renew_password);
                img_showpass_old = dialog.findViewById(R.id.icon_showPassword_old);
                img_showpass_new = dialog.findViewById(R.id.icon_showPassword_new);
                img_showpass_renew = dialog.findViewById(R.id.icon_showPassword_renew);
                TextView btnBack = dialog.findViewById(R.id.btnCancelUpdate_password);
                TextView btnUpdatePass = dialog.findViewById(R.id.btnUpdatePassword);

                img_showpass_old.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPass_old();
                    }
                });

                img_showpass_new.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPass_new();
                    }
                });

                img_showpass_renew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPass_renew();
                    }
                });

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnUpdatePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (check()) {
                            String id = model.get_id();
                            String old_pass = edt_old_pass.getText().toString().trim();
                            String new_pass = edt_new_pass.getText().toString().trim();
                            User user = new User(old_pass, new_pass);
                            updatePasswordUser(token, id, user);
                        }
                    }
                });


                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có chắc muốn đăng xuất không?");


                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog dialog1 = new ProgressDialog(getActivity());
                        dialog1.setMessage("Đang đăng xuất...");
                        dialog1.show();
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    sleep(1444);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    logout_inApp(dialog);
                                }
                            }
                        };
                        thread.start();

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

    private void logout_inApp(DialogInterface dialog) {
        SharedPreferences preferences = getActivity().getSharedPreferences("user-login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_login", false);
        editor.commit();

        String token_user = preferences.getString("token", "");

        RetrofitClient.managerServices().logout(token_user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    dialog.dismiss();
                    getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void showPass_old() {
        if (password_old_Showing) {
            password_old_Showing = false;
            edt_old_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_showpass_old.setImageResource(R.drawable.ic_show_password);
        } else {
            password_old_Showing = true;
            edt_old_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_showpass_old.setImageResource(R.drawable.ic_hide_password);
        }
        edt_old_pass.setSelection(edt_old_pass.length());
    }

    private void showPass_new() {
        if (pass_new_showing) {
            pass_new_showing = false;
            edt_new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_showpass_new.setImageResource(R.drawable.ic_show_password);
        } else {
            pass_new_showing = true;
            edt_new_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_showpass_new.setImageResource(R.drawable.ic_hide_password);
        }
        edt_new_pass.setSelection(edt_new_pass.length());
    }

    private void showPass_renew() {
        if (pass_renew_showing) {
            pass_renew_showing = false;
            edt_renew_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            img_showpass_renew.setImageResource(R.drawable.ic_show_password);
        } else {
            pass_renew_showing = true;
            edt_renew_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_showpass_renew.setImageResource(R.drawable.ic_hide_password);
        }
        edt_renew_pass.setSelection(edt_renew_pass.length());
    }

    private boolean check() {
        if (edt_old_pass.length() == 0) {
            Toast.makeText(getContext(), "Chưa nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
            edt_old_pass.requestFocus();
            return false;
        }

        if (edt_new_pass.length() == 0) {
            Toast.makeText(getContext(), "Chưa nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            edt_new_pass.requestFocus();
            return false;
        }

        if (edt_renew_pass.length() == 0) {
            Toast.makeText(getContext(), "Chưa nhập lại mật khẩu mới", Toast.LENGTH_SHORT).show();
            edt_renew_pass.requestFocus();
            return false;
        }

        if (!edt_new_pass.getText().toString().trim().equals(edt_renew_pass.getText().toString().trim())) {
            Toast.makeText(getContext(), "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updatePasswordUser(String token, String id, User user) {

        RetrofitClient.servicesNoCookie().updatePassword(token, id, user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    edt_old_pass.setText("");
                    edt_new_pass.setText("");
                    edt_renew_pass.setText("");

                    SharedPreferences preferences = getActivity().getSharedPreferences("user-login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("is_login", false);
                    editor.commit();

                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else if (response.code() == 303) {
                    Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    edt_old_pass.requestFocus();
                } else {
                    ResMessage resMessage = response.body();
                    System.out.println(resMessage);
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("func updatePassword response: " + t);
            }
        });
    }

    void reloadData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RetrofitClient.managerServices().profile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    model = response.body();
                    tv_fullname.setText(model.getFullname());

                    Glide.with(getContext())
                            .load(API_image + model.getImage())
                            .into(img);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("token" + t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
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