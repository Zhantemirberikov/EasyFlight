package com.example.easyflight;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnRegister, btnGoToLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.register_email);
        inputPassword = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.btn_register);
        btnGoToLogin = findViewById(R.id.btn_go_to_login);
        progressBar = findViewById(R.id.register_progress);

        btnRegister.setOnClickListener(v -> registerUser());
        btnGoToLogin.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void registerUser() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Введите email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Введите пароль");
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("Минимум 6 символов");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
