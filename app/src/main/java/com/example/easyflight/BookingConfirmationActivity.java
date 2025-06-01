package com.example.easyflight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingConfirmationActivity extends AppCompatActivity {

    private TextView ticketInfo;
    private Button btnGoHome, btnSavePdf;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FlightBookings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ваш билет");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ticketInfo = findViewById(R.id.ticket_info);
        btnGoHome = findViewById(R.id.btn_go_home);
        btnSavePdf = findViewById(R.id.btn_save_pdf);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        FlightResponse.Data flight = (FlightResponse.Data) getIntent().getSerializableExtra("flight");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (flight != null && user != null) {
            String ticket = "🎟 Билет подтвержден\n\n"
                    + "👤 Пассажир: " + user.getEmail() + "\n"
                    + "✈️ Рейс: " + flight.airline.name + " " + flight.flight.number + "\n"
                    + "📍 Маршрут: " + flight.departure.airport + " → " + flight.arrival.airport + "\n"
                    + "🕒 Вылет: " + flight.departure.scheduled;

            ticketInfo.setText(ticket);
            saveTicket(user.getEmail(), flight);
        } else {
            ticketInfo.setText("Ошибка загрузки билета.");
        }

        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(BookingConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnSavePdf.setOnClickListener(v -> saveTicketAsPdf(ticketInfo.getText().toString()));
    }

    private void saveTicket(String email, FlightResponse.Data flight) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        String data = "Пользователь: " + email + "\n"
                + "Рейс: " + flight.airline.name + " " + flight.flight.number + "\n"
                + "Маршрут: " + flight.departure.airport + " → " + flight.arrival.airport + "\n"
                + "Вылет: " + flight.departure.scheduled + "\n"
                + "Зарегистрировано: " + time + "\n---\n";

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String existing = sharedPreferences.getString("tickets", "");
        editor.putString("tickets", existing + data);
        editor.apply();
    }

    private void saveTicketAsPdf(String content) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int y = 50;
        for (String line : content.split("\n")) {
            canvas.drawText(line, 40, y, paint);
            y += 25;
        }

        pdfDocument.finishPage(page);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "easyflight_ticket_" + System.currentTimeMillis() + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF сохранён в папку Downloads", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка сохранения PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        pdfDocument.close();
    }
}
