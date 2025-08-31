package playwright.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BankAccount {

    private String name;
    private LocalDate startDate;
    private Double startBalance;
}
