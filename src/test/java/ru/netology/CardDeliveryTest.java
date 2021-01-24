package ru.netology;

import com.github.javafaker.Faker;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void openWeb() {
        open("http://localhost:9999");
    }

    @Test
    void shouldArrangeDelivery() {
        val userData = DataGenerator.generateByGeneralData("ru");
        $("[data-test-id='city'] input").setValue(userData.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(userData.getDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] input").setValue(userData.getName());
        $("[data-test-id='phone'] input").setValue(userData.getPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='success-notification']").waitUntil(visible, 15000)
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " +
                        userData.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(userData.getDate().plusDays(6)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("button.button").click();

        $("[data-test-id='replan-notification']").waitUntil(visible, 15000)
                .shouldHave(exactText("Необходимо подтверждение " +
                        "У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification']").waitUntil(visible, 15000)
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " +
                        userData.getDate().plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}
