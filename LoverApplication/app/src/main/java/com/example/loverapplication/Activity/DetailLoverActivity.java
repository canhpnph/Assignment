package com.example.loverapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loverapplication.Adapter.Adapter_Spinner;
import com.example.loverapplication.Model.Lover;
import com.example.loverapplication.Model.LoverType;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLoverActivity extends AppCompatActivity {
    TextView tv_name, tv_age, tv_phone, tv_weight, tv_height, tv_about, tv_type, btnOpenDialogUpdate;
    LinearLayout btnBack;
    ImageView img_avatar, img_avatar_ud;
    EditText edt_name, edt_age, edt_height, edt_weight, edt_about, edt_phone;
    SwipeRefreshLayout refresh;
    String API_image = "http://192.168.1.4:3000/uploads/";
    private static final int REQUEST_CALL_PHONE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSON_CODE = 11;

    String imagePath = null;
    Lover model;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lover);

        tv_name = findViewById(R.id.tv_name_detail);
        tv_age = findViewById(R.id.tv_age_detail);
        tv_phone = findViewById(R.id.tv_phone_detail);
        tv_weight = findViewById(R.id.tv_weight_detail);
        tv_height = findViewById(R.id.tv_height_detail);
        tv_about = findViewById(R.id.tv_about_detail);
        tv_type = findViewById(R.id.tv_type_detail);
        refresh = findViewById(R.id.refresh_detailLover);
        btnBack = findViewById(R.id.btnBack_to_ListLover);
        btnOpenDialogUpdate = findViewById(R.id.btnOpenDialogUpdateInfoLover);
        img_avatar = findViewById(R.id.img_avt_detail);


        reloadDataAfterUpdateModel();

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCall(model.getPhone());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnOpenDialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdate(model);
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadDataAfterUpdateModel();
                refresh.setRefreshing(false);
            }
        });

    }

    private void requestCall(String phoneNumber) {
        String phoneNumberWithDialer = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phoneNumberWithDialer));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCall(tv_phone.getText().toString().trim());
                } else {
                    Toast.makeText(this, "Không có quyền thực hiện cuộc gọi", Toast.LENGTH_SHORT).show();
                }

            case PERMISSON_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(this, "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showDialogUpdate(Lover model) {
        Dialog dialog = new Dialog(DetailLoverActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_update_info_lover);
        dialog.show();

        // ánh xạ
        TextView btnUpdate, btnCancel;
        Spinner spinner;

        img_avatar_ud = dialog.findViewById(R.id.img_avt_detail_upd);
        edt_name = dialog.findViewById(R.id.edt_name_detail_upd);
        edt_age = dialog.findViewById(R.id.edt_age_detail_upd);
        edt_height = dialog.findViewById(R.id.edt_height_detail_upd);
        edt_weight = dialog.findViewById(R.id.edt_weight_detail_upd);
        edt_about = dialog.findViewById(R.id.edt_about_detail_upd);
        edt_phone = dialog.findViewById(R.id.edt_phone_detail_upd);
        btnUpdate = dialog.findViewById(R.id.btnUpdateInfoLover);
        btnCancel = dialog.findViewById(R.id.btnCancelUpdate);
        spinner = dialog.findViewById(R.id.spinner_update_lover);

        reloadDataInDialog(spinner);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString();
                String age = edt_age.getText().toString().trim();
                String weight = edt_weight.getText().toString().trim();
                String height = edt_height.getText().toString();
                String phone = edt_phone.getText().toString();
                String about = edt_about.getText().toString();
                LoverType type = (LoverType) spinner.getSelectedItem();
                updateModel(model.get_id(), name, phone, age, weight, height, about, type, dialog);
                reloadDataAfterUpdateModel();
                reloadDataInDialog(spinner);
            }
        });

        img_avatar_ud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermisson_SelectImage_FromLirbrary();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void updateModel(String id, String name, String phone, String age, String weight, String height, String about, LoverType type, Dialog dialog) {
        MultipartBody.Part namePart = null;
        MultipartBody.Part phonePart = null;
        MultipartBody.Part agePart = null;
        MultipartBody.Part weightPart = null;
        MultipartBody.Part heightPart = null;
        MultipartBody.Part aboutPart = null;
        MultipartBody.Part typePart = null;

        if (check()) {
            namePart = MultipartBody.Part.createFormData("name", name);
            phonePart = MultipartBody.Part.createFormData("phone", phone);
            agePart = MultipartBody.Part.createFormData("age", age);
            weightPart = MultipartBody.Part.createFormData("weight", weight);
            heightPart = MultipartBody.Part.createFormData("height", height);
            aboutPart = MultipartBody.Part.createFormData("about", about);
            typePart = MultipartBody.Part.createFormData("type", type.get_id());

            if (imagePath != null) {
                File file = new File(imagePath);

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                RetrofitClient.managerServices().updateLover(id, namePart, phonePart, agePart, weightPart, heightPart, imagePart, aboutPart, typePart).enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getBaseContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        System.out.println(t);
                    }
                });
            } else {
                RetrofitClient.managerServices().updateLover(id, namePart, phonePart, agePart, weightPart, heightPart, null, aboutPart, typePart).enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getBaseContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {
                        System.out.println(t);
                    }
                });
            }

        }


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
            img_avatar_ud.setImageURI(selectedImageUri);
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

    public boolean check() {
        if (edt_name.length() == 0) {
            Toast.makeText(this, "Chưa nhập họ tên", Toast.LENGTH_SHORT).show();
            edt_name.requestFocus();
            return false;
        }

        if (edt_age.getText().toString().equals("")) {
            Toast.makeText(this, "Chưa nhập tuổi", Toast.LENGTH_SHORT).show();
            edt_age.requestFocus();
            return false;
        } else {
            if (Integer.parseInt(edt_age.getText().toString()) <= 0 && Integer.parseInt(edt_age.getText().toString()) > 120) {
                Toast.makeText(this, "Tuổi phải lớn hơn 0 và nhỏ hơn 120", Toast.LENGTH_SHORT).show();
                edt_age.requestFocus();
                return false;
            }
        }

        if (edt_height.length() == 0) {
            Toast.makeText(this, "Chưa nhập chiều cao", Toast.LENGTH_SHORT).show();
            edt_height.requestFocus();
            return false;
        }

        if (edt_weight.getText().toString().equals("")) {
            Toast.makeText(this, "Chưa nhập cân nặng", Toast.LENGTH_SHORT).show();
            edt_weight.requestFocus();
            return false;
        } else {
            if (Integer.parseInt(edt_weight.getText().toString().trim()) <= 0 && Integer.parseInt(edt_weight.getText().toString().trim()) > 200) {
                Toast.makeText(this, "Cân nặng phải lớn hơn 0 và nhỏ hơn 200", Toast.LENGTH_SHORT).show();
                edt_weight.requestFocus();
                return false;
            }
        }

        if (edt_phone.length() == 0) {
            Toast.makeText(this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
            edt_phone.requestFocus();
            return false;
        } else {
            if (edt_phone.length() == 10) {
                if (!validateNumberPhone(edt_phone.getText().toString().trim())) {
                    Toast.makeText(this, "Số điện thoại phải bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "Số điện thoại phải có đủ 10 số", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (edt_about.length() == 0) {
            Toast.makeText(this, "Chưa nhập nội dung mô tả", Toast.LENGTH_SHORT).show();
            edt_about.requestFocus();
            return false;
        }

        return true;
    }

    public boolean validateNumberPhone(String numberphone) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^0([0-9]{9})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(numberphone);
        return matcher.matches();
    }

    private void reloadDataAfterUpdateModel() {

        Intent intent = getIntent();
        model = new Lover();
        model = intent.getParcelableExtra("model");

        RetrofitClient.managerServices().getListLover(model.get_id()).enqueue(new Callback<List<Lover>>() {
            @Override
            public void onResponse(Call<List<Lover>> call, Response<List<Lover>> response) {
                if (response.isSuccessful()) {
                    List<Lover> list = response.body();
                    Lover model = list.get(0);

                    Glide.with(getBaseContext())
                            .load(API_image + model.getImage())
                            .into(img_avatar);

                    tv_name.setText(model.getName());
                    tv_age.setText(model.getAge());
                    tv_phone.setText(model.getPhone());
                    tv_weight.setText(model.getWeight());
                    tv_height.setText(model.getHeight());
                    tv_about.setText(model.getAbout());
                    tv_type.setText(model.getType().getName());
                }
            }

            @Override
            public void onFailure(Call<List<Lover>> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadDataAfterUpdateModel();
    }

    private void reloadDataInDialog(Spinner spinner) {
        Intent intent = getIntent();
        model = new Lover();
        model = intent.getParcelableExtra("model");

        RetrofitClient.managerServices().getListLover(model.get_id()).enqueue(new Callback<List<Lover>>() {
            @Override
            public void onResponse(Call<List<Lover>> call, Response<List<Lover>> response) {
                if (response.isSuccessful()) {
                    List<Lover> list = response.body();
                    Lover model = list.get(0);

                    Glide.with(getBaseContext())
                            .load(API_image + model.getImage())
                            .into(img_avatar_ud);
                    edt_name.setText(model.getName());
                    edt_age.setText(model.getAge());
                    edt_about.setText(model.getAbout());
                    edt_height.setText(model.getHeight());
                    edt_weight.setText(model.getWeight());
                    edt_phone.setText(model.getPhone());

                    RetrofitClient.managerServices().getListLoverType().enqueue(new Callback<List<LoverType>>() {
                        @Override
                        public void onResponse(Call<List<LoverType>> call, Response<List<LoverType>> response) {
                            if (response.isSuccessful()) {
                                List<LoverType> listLoverType = response.body();

                                Adapter_Spinner adapter = new Adapter_Spinner(getBaseContext(), listLoverType);
                                spinner.setAdapter(adapter);

                                for (int i = 0; i < listLoverType.size(); i++) {
                                    LoverType type = listLoverType.get(i);
                                    if (type.get_id().equals(model.getType().get_id())) {
                                        spinner.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<LoverType>> call, Throwable t) {
                            System.out.println(t);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Lover>> call, Throwable t) {
            }
        });

    }
}