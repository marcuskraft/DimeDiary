package playwright.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Transaction {

    private LocalDate localDate;
    private String name;
    private String category;
    private Double amount;
    private Double balance;

}
