package playwright.utils;

import com.microsoft.playwright.Locator;

public class AmountUtils {

    public static void setAmount(Double amount, Locator locator) {
        String amountString = String.format("%.2f", amount);
        locator.fill("");
        locator.pressSequentially(amountString, new Locator.PressSequentiallyOptions().setDelay(50));
    }

    public static String normalizeAmountString(String amount) {
        return amount
                .replace(" ", "")
                .replace("€", "")
                .replace(".", "")
                .replace(",", ".");
    }

}
