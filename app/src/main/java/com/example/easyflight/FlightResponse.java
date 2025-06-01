package com.example.easyflight;

import java.io.Serializable;
import java.util.List;

public class FlightResponse {
    public List<Data> data;

    public static class Data implements Serializable {
        public Airline airline;
        public FlightDetails flight;
        public Airport departure;
        public Airport arrival;

        public static class Airline implements Serializable {
            public String name;
        }

        public static class FlightDetails implements Serializable {
            public String number;
        }

        public static class Airport implements Serializable {
            public String airport;
            public String scheduled;
        }

        @Override
        public String toString() {
            return airline.name + " " + flight.number + "\n" +
                    departure.airport + " → " + arrival.airport + "\n" +
                    "Вылет: " + departure.scheduled;
        }
    }
}
