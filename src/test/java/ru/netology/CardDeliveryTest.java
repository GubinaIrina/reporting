package ru.netology;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    private Faker faker;
    LocalDate date = LocalDate.now();

    @BeforeEach
    void setUpAll() {
        faker = new Faker(new Locale("ru"));
        open("http://localhost:9999");
    }

    @Test
    void shouldArrangeDelivery() {
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();
        $("[data-test-id='city'] input").setValue("Мо");
        $(".menu_mode_radio-check").findElement(withText("Москва")).click();
        $("[data-test-id='date'] input").click();
        LocalDate date = LocalDate.now();
        LocalDate currentDate = date.plusDays(3);
        LocalDate dateTest = date.plusDays(7);
        $("[data-test-id='date'] input").click();
        if (dateTest.getYear() > currentDate.getYear()) {
            $("[data-step=\"12\"]").click();
        }
        if (!dateTest.getMonth().equals(currentDate.getMonth())) {
            $("[data-step=\"1\"]").click();
        }
        $("table").findElement(withText(String.valueOf(dateTest.getDayOfMonth()))).click();
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='success-notification']").waitUntil(visible, 15000)
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " +
                        dateTest.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }

    @Test
    void shouldToRePlanMeeting() {
        shouldArrangeDelivery();

        LocalDate newDateTest = date.plusDays(10);
        $("[data-test-id='date'] input").click();
        $("table").findElement(withText(String.valueOf(newDateTest.getDayOfMonth()))).click();
        $("button.button").click();

        $("[data-test-id='replan-notification']").waitUntil(visible, 15000)
                .shouldHave(exactText("Необходимо подтверждение " +
                        "У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification']").waitUntil(visible, 15000)
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " +
                        newDateTest.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}
