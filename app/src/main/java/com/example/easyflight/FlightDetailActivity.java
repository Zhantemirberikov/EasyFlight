package com.example.easyflight;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FlightDetailActivity extends AppCompatActivity {

    private TextView flightInfo;
    private Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Детали рейса");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        flightInfo = findViewById(R.id.flight_info);
        btnBook = findViewById(R.id.btn_book);

        FlightResponse.Data flight = (FlightResponse.Data) getIntent().getSerializableExtra("flight");

        if (flight != null) {
            String details = "✈️ Рейс: " + flight.airline.name + " " + flight.flight.number + "\n"
                    + "📍 Маршрут: " + flight.departure.airport + " → " + flight.arrival.airport + "\n"
                    + "🕒 Вылет: " + flight.departure.scheduled;
            flightInfo.setText(details);

            btnBook.setOnClickListener(v -> {
                Intent intent = new Intent(FlightDetailActivity.this, BookingConfirmationActivity.class);
                intent.putExtra("flight", flight);
                startActivity(intent);
            });
        } else {
            flightInfo.setText("Ошибка загрузки данных.");
            btnBook.setEnabled(false);
        }
    }
}
