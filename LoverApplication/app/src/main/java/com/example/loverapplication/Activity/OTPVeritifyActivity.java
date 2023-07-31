package com.example.loverapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loverapplication.Model.Lover;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;
import com.example.loverapplication.R;
import com.example.loverapplication.Retrofit.LoverManagerServices;
import com.example.loverapplication.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPVeritifyActivity extends AppCompatActivity {
    EditText otp1, otp2, otp3, otp4;
    AppCompatButton btnVerify;
    TextView btnResendCode, tv_phone;
    boolean resendEnable = false;
    int selectedETPosition = 0;
    String avatar_default = "avatar_default.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpveritify);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        btnVerify = findViewById(R.id.btnVerify);
        btnResendCode = findViewById(R.id.btnResendCodeOTP);
        tv_phone = findViewById(R.id.tv_phone_verify);

        final String username = getIntent().getStringExtra("username");
        final String password = getIntent().getStringExtra("password");
        final String phonenumber_verify = getIntent().getStringExtra("phone");
        tv_phone.setText(phonenumber_verify);

        final User user = new User(username, "", "", phonenumber_verify, password, avatar_default );

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);

        showKeyBoard(otp1);

        startCountDownTimer();

        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnable) {
                    // start new resend code
                    startCountDownTimer();
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String otp = otp1.getText().toString() + otp2.getText().toString()
                        + otp3.getText().toString() + otp4.getText().toString();

                if (otp.length() == 4) {
                    register_Account(user);
                }
            }
        });
    }

    final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                if (selectedETPosition == 0) {
                    selectedETPosition = 1;
                    showKeyBoard(otp2);
                } else if (selectedETPosition == 1) {
                    selectedETPosition = 2;
                    showKeyBoard(otp3);
                } else if (selectedETPosition == 2) {
                    selectedETPosition = 3;
                    showKeyBoard(otp4);
                }
            }
        }
    };

    void showKeyBoard(EditText edt) {
        edt.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
    }

    void startCountDownTimer() {
        resendEnable = false;
        btnResendCode.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long mili) {
                btnResendCode.setText("Gửi lại (" + (mili / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                btnResendCode.setText("Gửi lại");
                btnResendCode.setTextColor(getResources().getColor(R.color.primary));
            }
        }.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (selectedETPosition == 3) {
                selectedETPosition = 2;
                showKeyBoard(otp3);
            } else if (selectedETPosition == 2) {
                selectedETPosition = 1;
                showKeyBoard(otp2);
            } else if (selectedETPosition == 1) {
                selectedETPosition = 0;
                showKeyBoard(otp1);
            }

            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    private void register_Account(User user) {
        RetrofitClient.servicesNoCookie().register(user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 201) {
                    Toast.makeText(OTPVeritifyActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getSharedPreferences("user-login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", user.getUsername());
                    editor.putString("password", user.getPassword());
                    editor.commit();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}