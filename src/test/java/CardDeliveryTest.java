import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.Keys.*;

class CardDeliveryTest {
    private String setDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void ShouldSucceed() {
        Selenide.open("http://0.0.0.0:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        String date = setDate(3, "dd.MM.yyyy");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Бла бла Бла бла");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + date));

    }

    @Test
    void ShouldFail_Name() {
        Selenide.open("http://0.0.0.0:9999");
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        String date = setDate(3, "dd.MM.yyyy");
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue("Latin Letters");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

        assertEquals("http://0.0.0.0:9999/", url());

    }

}
