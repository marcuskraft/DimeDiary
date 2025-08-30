package playwright.pages;

import com.microsoft.playwright.Page;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import playwright.base.BaseSystemtest;

@Configuration
public class PageFactory {

    @Bean
    public static Page getPage() {
        return BaseSystemtest.getPage();
    }

}
