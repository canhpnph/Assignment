package com.example.loverapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loverapplication.Adapter.Adapter_Spinner;
import com.example.loverapplication.Model.Lover;
import com.example.loverapplication.Model.LoverType;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewLoverActivity extends AppCompatActivity {
    ImageView img_avatar, ic_add;
    EditText edt_name, edt_age, edt_height, edt_weight, edt_about, edt_phone;
    Spinner spinner;
    TextView btnCancel, btnAdd;
    List<LoverType> listLoverType = new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSON_CODE = 11;

    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_lover);

        overridePendingTransition(R.anim.slide_in_dialog, R.anim.slide_out_dialog);

        edt_name = findViewById(R.id.edt_name_add);
        edt_age = findViewById(R.id.edt_age_add);
        edt_phone = findViewById(R.id.edt_phone_add);
        edt_weight = findViewById(R.id.edt_weight_add);
        edt_height = findViewById(R.id.edt_height_add);
        edt_about = findViewById(R.id.edt_about_add);
        spinner = findViewById(R.id.spinner_add);
        btnCancel = findViewById(R.id.btnCancelAdd);
        btnAdd = findViewById(R.id.btnAddNewModel);
        img_avatar = findViewById(R.id.img_avt_add);
        ic_add = findViewById(R.id.ic_add_in_image);

        RetrofitClient.managerServices().getListLoverType().enqueue(new Callback<List<LoverType>>() {
            @Override
            public void onResponse(Call<List<LoverType>> call, Response<List<LoverType>> response) {
                if (response.code() == 200) {
                    listLoverType = response.body();
                    Adapter_Spinner adapter = new Adapter_Spinner(AddNewLoverActivity.this, listLoverType);
                    spinner.setAdapter(adapter);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<LoverType>> call, Throwable t) {
                System.out.println(t);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences preferences = getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String id_user = preferences.getString("id", "");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString();
                String age = edt_age.getText().toString();
                String phone = edt_phone.getText().toString();
                String weight = edt_weight.getText().toString();
                String height = edt_height.getText().toString();
                String about = edt_about.getText().toString();
                LoverType type = (LoverType) spinner.getSelectedItem();
                addNewModel(name, phone, age, weight, height, about, type, id_user);
            }
        });

        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermisson_SelectImage_FromLirbrary();
            }
        });
    }

    private void addNewModel(String name, String phone, String age, String weight, String height, String about, LoverType type, String loverOf) {

        if (check()) {
            if (imagePath != null) {
                File file = new File(imagePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                MultipartBody.Part namePart = MultipartBody.Part.createFormData("name", name);
                MultipartBody.Part phonePart = MultipartBody.Part.createFormData("phone", phone);
                MultipartBody.Part agePart = MultipartBody.Part.createFormData("age", age);
                MultipartBody.Part weightPart = MultipartBody.Part.createFormData("weight", weight);
                MultipartBody.Part heightPart = MultipartBody.Part.createFormData("height", height);
                MultipartBody.Part aboutPart = MultipartBody.Part.createFormData("about", about);
                MultipartBody.Part typePart = MultipartBody.Part.createFormData("type", type.get_id());
                MultipartBody.Part loverOfPart = MultipartBody.Part.createFormData("loverOf", loverOf);

                RetrofitClient.managerServices().addNewLover(namePart, phonePart, agePart, weightPart, heightPart, imagePart, aboutPart, typePart, loverOfPart)
                        .enqueue(new Callback<ResMessage>() {
                            @Override
                            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(AddNewLoverActivity.this, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResMessage> call, Throwable t) {
                                System.out.println(t);
                            }
                        });
            } else {
                Toast.makeText(this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
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
            img_avatar.setImageURI(selectedImageUri);
            ic_add.setVisibility(View.GONE);
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

}