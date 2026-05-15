package mk.ukim.finki.wp.kol2025g3.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity //bidejki e entitet
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //obavezno id i generatedvalue
    private Long id;

    private String title;

    private LocalDate dateCreated;

    private Double amount;

    private Integer daysToExpire;

    private ExpenseCategory expenseCategory;

    @ManyToOne //eden troshok e od tocno eden vendor, eden vendor ima povekje troshoci
    private Vendor vendor;

    public Expense(String title, LocalDate dateCreated, Double amount, Integer daysToExpire, ExpenseCategory expenseCategory, Vendor vendor) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.amount = amount;
        this.daysToExpire = daysToExpire;
        this.expenseCategory = expenseCategory;
        this.vendor = vendor;
    }

}
