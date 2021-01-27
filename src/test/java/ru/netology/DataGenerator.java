package ru.netology;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }
    }

    public static UserDataForDelivery generateByGeneralData(String locale) {
        Faker faker = new Faker(new Locale("ru"));
        return new UserDataForDelivery(
                CityMeeting(),
                faker.name().fullName(),
                faker.phoneNumber().phoneNumber()
        );
    }

    public static String DateMeeting(int days) {
        LocalDate dateMeeting = LocalDate.now();
        return dateMeeting.plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String CityMeeting() {
        String[] city = {"Москва", "Уфа", "Чебоксары", "Пермь", "Волгоград", "Мурманск", "Санкт-Петербург",
                "Ярославль", "Тула", "Екатеринбург", "Самара"};
        int idx = new Random().nextInt(city.length);
        String cityMeeting = (city[idx]);
        return cityMeeting;
    }
}
