package playwright.pages;

import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionOverviewComponent {

    private Page page;


    public boolean isVisible() {
        return page.isVisible("[data-ref=transactions-overview]");
    }

    public void assertVisibility() {
        page.locator("[data-ref=transactions-overview]").waitFor();
    }
}
