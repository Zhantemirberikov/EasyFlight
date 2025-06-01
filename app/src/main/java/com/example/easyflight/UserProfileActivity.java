package com.example.easyflight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView emailTextView;
    private Button btnLogout, btnBack, btnHistory, btnToggleTheme;
    private SharedPreferences themePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        themePrefs = getSharedPreferences("theme", MODE_PRIVATE);
        boolean isDark = themePrefs.getBoolean("dark_theme", false);
        AppCompatDelegate.setDefaultNightMode(isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Профиль");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        emailTextView = findViewById(R.id.user_email);
        btnLogout = findViewById(R.id.btn_logout);
        btnBack = findViewById(R.id.btn_back);
        btnHistory = findViewById(R.id.btn_history);
        btnToggleTheme = findViewById(R.id.btn_toggle_theme);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailTextView.setText("Ваш email: " + user.getEmail());
        } else {
            emailTextView.setText("Пользователь не найден");
        }

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });

        btnBack.setOnClickListener(v -> finish());

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, BookingHistoryActivity.class);
            startActivity(intent);
        });

        btnToggleTheme.setOnClickListener(v -> {
            boolean currentlyDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
            int newMode = currentlyDark ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;

            AppCompatDelegate.setDefaultNightMode(newMode);
            themePrefs.edit().putBoolean("dark_theme", newMode == AppCompatDelegate.MODE_NIGHT_YES).apply();
            recreate(); // перезапускаем активити
        });
    }
}
