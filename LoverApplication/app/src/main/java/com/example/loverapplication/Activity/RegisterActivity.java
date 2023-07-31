package com.example.loverapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.RetrofitClient;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_username, edt_password, edt_confirm_password, edt_phone;
    AppCompatButton btnRegister;
    TextView btnBacktoLogin;
    ImageView img_showPass, img_confirm_showPass;
    private boolean passwordShowing = false;
    private boolean passwordConfirmShowing = false;
    private boolean checkUsername = true;
    private boolean checkPhone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_password = findViewById(R.id.edt_password_register);
        edt_confirm_password = findViewById(R.id.edt_confirm_password_register);
        edt_username = findViewById(R.id.edt_username_register);
        edt_phone = findViewById(R.id.edt_phone_register);
        btnRegister = findViewById(R.id.btnRegister);
        btnBacktoLogin = findViewById(R.id.btnLogin_register);
        img_showPass = findViewById(R.id.icon_showPassword_register);
        img_confirm_showPass = findViewById(R.id.icon_confirm_showPassword_register);


        btnBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                onBackPressed();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidate()) {
//                    checkPhoneNumber(new User(edt_username.getText().toString().trim(),
//                            edt_phone.getText().toString().trim(), "", ""));
                    checkUsername(new User(edt_username.getText().toString().trim(),
                            edt_phone.getText().toString().trim(), "", ""));
                }
            }
        });

        img_showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    img_showPass.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordShowing = true;
                    edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    img_showPass.setImageResource(R.drawable.ic_hide_password);
                }
                edt_password.setSelection(edt_password.length());
            }
        });

        img_confirm_showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordConfirmShowing) {
                    passwordConfirmShowing = false;
                    edt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    img_confirm_showPass.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordConfirmShowing = true;
                    edt_confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    img_confirm_showPass.setImageResource(R.drawable.ic_hide_password);
                }
                edt_confirm_password.setSelection(edt_confirm_password.length());
            }
        });

    }

    public boolean checkValidate() {
        if (edt_username.length() == 0) {
            Toast.makeText(this, "Chưa nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            edt_username.requestFocus();
            return false;
        } else {
            if (edt_username.length() == 1) {
                Toast.makeText(this, "Tên đăng nhập tối thiểu 2 kí tự", Toast.LENGTH_SHORT).show();
                edt_username.requestFocus();
                return false;
            } else if (edt_username.length() >= 16) {
                Toast.makeText(this, "Tên đăng nhập tối đa 15 kí tự", Toast.LENGTH_SHORT).show();
                edt_username.requestFocus();
                return false;
            } else {
                if (!validateUsername(edt_username.getText().toString().trim())) {
                    Toast.makeText(this, "Tên đăng nhập không được phép có kí tự đặc biệt", Toast.LENGTH_SHORT).show();
                    edt_username.requestFocus();
                    return false;
                }
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
                    edt_phone.requestFocus();
                    return false;
                }
            } else {
                Toast.makeText(this, "Số điện thoại phải có đủ 10 số", Toast.LENGTH_SHORT).show();
                edt_phone.requestFocus();
                return false;
            }
        }

        if (edt_password.length() == 0) {
            Toast.makeText(this, "Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            edt_password.requestFocus();
            return false;
        }

        if (edt_confirm_password.length() == 0) {
            Toast.makeText(this, "Chưa nhập xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
            edt_confirm_password.requestFocus();
            return false;
        }

        if (!edt_password.getText().toString().trim().equals(edt_confirm_password.getText().toString().trim())) {
            Toast.makeText(this, "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
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

    public boolean validateUsername(String username) {
        Pattern pattern;
        Matcher matcher;
        final String Username_Pattern = "^[a-zA-Z0-9]{2,15}$";
        pattern = Pattern.compile(Username_Pattern);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean checkUsername(User user) {
        RetrofitClient.servicesNoCookie().checkUserName(user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 210) {
                    Toast.makeText(RegisterActivity.this, "Tên đăng nhập " + user.getUsername() + " đã được sử dụng", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(RegisterActivity.this, OTPVeritifyActivity.class);
                    intent.putExtra("username", edt_username.getText().toString().trim());
                    intent.putExtra("phone", edt_phone.getText().toString().trim());
                    intent.putExtra("password", edt_password.getText().toString().trim());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println(t);
            }
        });

        return true;
    }

}