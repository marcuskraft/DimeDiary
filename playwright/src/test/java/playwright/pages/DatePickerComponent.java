package playwright.pages;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DatePickerComponent {

    public static final String V_PICKER_DATE = ".v-picker--date";
    public static final String V_DATE_PICKER_HEADER_VALUE = ".v-date-picker-header__value";
    public static final String BUTTON = "button";
    public static final String V_DATE_PICKER_YEARS = ".v-date-picker-years";
    public static final String YEARS = "li:has-text('<year>')";
    public static final String MONTHS = "div:has-text('<month>')";
    public static final DateTimeFormatter GERMAN_MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM", Locale.GERMAN);
    public static final String V_DATE_PICKER_TABLE_MONTH = ".v-date-picker-table--month";
    public static final String DATE_PICKER_TABLE_DATE = ".v-date-picker-table--date";
    public static final String DAYS = ".v-btn__content:has-text('<day>')";
    private PageFactory pageFactory;

    public void setDate(LocalDate localDate) {
        Page page = pageFactory.getPage();

        Locator datePicker = page.locator(V_PICKER_DATE);

        // open year selection (twice to come to the year selection)
        datePicker
                .locator(V_DATE_PICKER_HEADER_VALUE)
                .locator(BUTTON)
                .first()
                .click();
        datePicker
                .locator(V_DATE_PICKER_HEADER_VALUE)
                .locator(BUTTON)
                .first()
                .click();

        // select year
        String year = String.valueOf(localDate.getYear());
        datePicker
                .locator(V_DATE_PICKER_YEARS)
                .locator(YEARS.replace("<year>", year)).click();

        // select month
        String month = localDate.format(GERMAN_MONTH_FORMATTER).substring(0, 3).toUpperCase();
        datePicker
                .locator(V_DATE_PICKER_TABLE_MONTH)
                .locator(MONTHS.replace("<month>", month)).click();

        // select day
        String day = String.valueOf(localDate.getDayOfMonth());
        datePicker
                .locator(DATE_PICKER_TABLE_DATE)
                .locator(DAYS.replace("<day>", day)).all().stream()
                .filter(locator -> day.equals(locator.innerText()))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No Element for that day found"))
                .click();

    }
}
