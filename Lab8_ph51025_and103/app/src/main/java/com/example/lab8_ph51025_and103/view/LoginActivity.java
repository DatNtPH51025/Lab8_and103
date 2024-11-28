package com.example.lab8_ph51025_and103.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab8_ph51025_and103.R;
import com.example.lab8_ph51025_and103.databinding.ActivityLoginBinding;
import com.example.lab8_ph51025_and103.model.Response;
import com.example.lab8_ph51025_and103.model.User;
import com.example.lab8_ph51025_and103.services.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();

        userListener();
        TextView tvRe = findViewById(R.id.tvRes);
        tvRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void userListener() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _username = binding.edUsername.getText().toString().trim();
                String _password = binding.edPassword.getText().toString().trim();
                if (_username.isEmpty() | _password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật kẩu", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User();
                    user.setUsername(_username);
                    user.setPassword(_password);
                    httpRequest.callAPI().login(user).enqueue(responseUser);


                }
            }
        });
    }

    Callback<Response<User>> responseUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            Log.d("zzzzzzz", "onResponse: "+ response.isSuccessful());
            if (response.isSuccessful()) {
                Log.d("zzzzzzz", "onResponse: " + response.body().getStatus());
                if (response.body().getStatus() ==200) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("INFO",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Sai tài khoản khoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            t.getMessage();
        }
    };
}