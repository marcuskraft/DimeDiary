package playwright.pages;

import com.microsoft.playwright.Page;
import org.springframework.context.annotation.Configuration;
import playwright.base.BaseSystemtest;

@Configuration
public class PageFactory {


    public Page getPage() {
        return BaseSystemtest.getPage();
    }

}
