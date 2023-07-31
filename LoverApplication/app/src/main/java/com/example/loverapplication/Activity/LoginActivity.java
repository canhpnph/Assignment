package com.example.loverapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.CookieManager;
import com.example.loverapplication.Retrofit.MyAppContext;
import com.example.loverapplication.Retrofit.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edt_username, edt_password;
    AppCompatButton btnLogin;
    TextView btnRegister;
    ImageView img_showPass;
    CheckBox checkBox;
    private boolean passwordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyAppContext.setContext(this);

        edt_password = findViewById(R.id.edt_password_login);
        edt_username = findViewById(R.id.edt_username_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister_login);
        img_showPass = findViewById(R.id.icon_showPassword_login);
        checkBox = findViewById(R.id.checkbox_saveInfo_login);

        SharedPreferences preferences = getSharedPreferences("user-login", Context.MODE_PRIVATE);
        String username_login = preferences.getString("username", "");
        String password_login = preferences.getString("password", "");
        boolean checkbox_login = preferences.getBoolean("checkbox", false);
        boolean is_login = preferences.getBoolean("is_login", false);

        if (is_login) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if (!username_login.isEmpty() && !password_login.isEmpty()) {
            edt_username.setText(username_login);
            edt_password.setText(password_login);
            checkBox.setChecked(checkbox_login);
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidate()) {
                    func_login(edt_username.getText().toString().trim(), edt_password.getText().toString().trim());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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

        if (edt_password.length() == 0) {
            Toast.makeText(this, "Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            edt_password.requestFocus();
            return false;
        }

        return true;
    }

    public boolean validateUsername(String username) {
        Pattern pattern;
        Matcher matcher;
        final String Username_Pattern = "^[a-zA-Z0-9]{2,15}$";
        pattern = Pattern.compile(Username_Pattern);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private void func_login(String username, String password) {
        RetrofitClient.servicesNoCookie().login(new User(username, password)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Headers headers = response.headers();
                    String cookies = headers.values("Set-Cookie").toString();

                    User user = response.body();
                    SharedPreferences preferences = getSharedPreferences("user-login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("is_login", true);
                    editor.putString("token", user.getToken());

                    CookieManager cookieManager = new CookieManager(getBaseContext());
                    cookieManager.saveCookie(cookies);

                    if (checkBox.isChecked()) {
                        editor.putString("id", user.get_id());
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("checkbox", true);
                        editor.commit();
                    } else {
                        edt_username.setText("");
                        edt_password.setText("");
                        checkBox.setChecked(false);
                        editor.clear();
                        editor.commit();
                    }

                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("LoginActivity - func-login response: " + t);
            }
        });
    }

}