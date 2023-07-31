package com.example.loverapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserActivity extends AppCompatActivity {
    String API_image = "http://192.168.1.4:3000/uploads/";
    LinearLayout btnBack;
    TextView btnShowDialogUpdateInfoUser;
    TextView tv_fullname, tv_phone, tv_date, tv_username;
    CircleImageView img_avatar, img_avatar_update;
    EditText edt_fullname, edt_date, edt_username, edt_phone;
    User model;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSON_CODE = 11;

    String imagePath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        btnBack = findViewById(R.id.btnBack_to_User);
        btnShowDialogUpdateInfoUser = findViewById(R.id.btnOpenDialogUpdateInfoUser);
        tv_date = findViewById(R.id.tv_date_user_detail);
        tv_fullname = findViewById(R.id.tv_fullname_user_detail);
        tv_phone = findViewById(R.id.tv_phone_user_detail);
        tv_username = findViewById(R.id.tv_username_detail);
        img_avatar = findViewById(R.id.img_avt_user_detail);

        SharedPreferences preferences = getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RetrofitClient.managerServices().profile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    model = response.body();
                    tv_fullname.setText(model.getFullname());
                    tv_username.setText(model.getUsername());
                    tv_phone.setText(model.getPhone());
                    tv_date.setText(model.getDate());

                    Glide.with(getBaseContext()).load(API_image + model.getImage()).into(img_avatar);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(t);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnShowDialogUpdateInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(InfoUserActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_update_info_user);
                dialog.show();

                TextView btnCancel = dialog.findViewById(R.id.btnCancelUpdate_user);
                TextView btnUpdate = dialog.findViewById(R.id.btnUpdateInfoUser);

                edt_fullname = dialog.findViewById(R.id.edt_fullname_detail_upd_user);
                edt_date = dialog.findViewById(R.id.edt_date_detail_upd_user);
                edt_username = dialog.findViewById(R.id.edt_username_detail_upd_user);
                edt_phone = dialog.findViewById(R.id.edt_phone_detail_upd_user);
                img_avatar_update = dialog.findViewById(R.id.img_avt_detail_upd_user);
                ImageView img_date = dialog.findViewById(R.id.icon_date_user);

                edt_fullname.setText(model.getFullname());
                edt_date.setText(model.getDate());
                edt_phone.setText(model.getPhone());
                edt_username.setText(model.getUsername());
                edt_phone.setEnabled(false);
                edt_username.setEnabled(false);

                Glide.with(getBaseContext()).load(API_image + model.getImage()).into(img_avatar_update);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateUser(model.getToken(), model.get_id(), edt_fullname.getText().toString(), edt_date.getText().toString());
                        reloadData();
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                img_avatar_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPermisson_SelectImage_FromLirbrary();
                    }
                });

                img_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectDate();
                    }
                });
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

    }

    private void updateUser(String token, String id, String fullname, String date) {
        MultipartBody.Part fullnamePart = null;
        MultipartBody.Part datePart = null;

        if (check()) {
            fullnamePart = MultipartBody.Part.createFormData("fullname", fullname);
            datePart = MultipartBody.Part.createFormData("date", date);
            if (imagePath != null) {
                File file = new File(imagePath);

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                RetrofitClient.managerServices().updateInfo(token, id, fullnamePart, datePart, imagePart).enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        Toast.makeText(InfoUserActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        System.out.println(t);
                    }
                });
            } else {
                RetrofitClient.managerServices().updateInfo(token, id, fullnamePart, datePart, null).enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        Toast.makeText(InfoUserActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        System.out.println(t);
                    }
                });
            }
        }

    }

    private boolean check() {
        if (edt_fullname.length() == 0) {
            edt_fullname.requestFocus();
            Toast.makeText(this, "Chưa nhập họ tên", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edt_date.length() == 0) {
            edt_date.requestFocus();
            Toast.makeText(this, "Chưa chọn ngày tháng năm sinh", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void checkPermisson_SelectImage_FromLirbrary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSON_CODE);
            } else {
                selectImage();
            }
        } else {
            selectImage();
        }
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imagePath = getRealPathFromUri(selectedImageUri); // Hàm để lấy đường dẫn thực sự của ảnh từ Uri
            img_avatar_update.setImageURI(selectedImageUri);
        }
    }

    // Hàm để lấy đường dẫn thực sự của ảnh từ Uri
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSON_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(this, "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void reloadData() {
        SharedPreferences preferences = getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RetrofitClient.managerServices().profile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    model = response.body();
                    tv_fullname.setText(model.getFullname());
                    tv_username.setText(model.getUsername());
                    tv_phone.setText(model.getPhone());
                    tv_date.setText(model.getDate());

                    Glide.with(getBaseContext()).load(API_image + model.getImage()).into(img_avatar);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(t);
            }
        });
    }


    public void selectDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        DatePickerDialog dialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int nam = i;
                        int thang = i1;
                        int ngay = i2;
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(nam, thang, ngay);
                        edt_date.setText(format.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );
        dialog.show();
    }
}