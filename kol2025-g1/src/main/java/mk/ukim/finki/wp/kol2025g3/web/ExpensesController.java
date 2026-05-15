package mk.ukim.finki.wp.kol2025g3.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2025g3.model.Expense;
import mk.ukim.finki.wp.kol2025g3.model.ExpenseCategory;
import mk.ukim.finki.wp.kol2025g3.service.ExpenseService;
import mk.ukim.finki.wp.kol2025g3.service.VendorService;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller //za Spring da znae deka ovaa klasa prima HTTP baranja i vrakja HTML view
@RequestMapping({"/", "/expenses"}) //site metodi vo ovaa klasa kje vazhat i za "/" i za "/expenses"
@AllArgsConstructor //za dependency injection
public class ExpensesController {
    private final ExpenseService expenseService; //tuka sekogas se koristi service i za dvete, ne smee da se koristi repository
    private final VendorService vendorService;

    /**
     * This method should use the "list.html" template to display all products.
     * The method should be mapped on paths '/' and '/expenses'.
     * The arguments that this method takes are optional and can be 'null'.
     * The filtered expenses that are the result of the call
     * findPage method from the ExpenseService should be displayed
     * If you want to return a paginated result, you should also pass the page number and the page size as arguments.
     *
     * @param title           The title which the expenses should contain
     * @param expenseCategory The category of the expenses
     * @param vendor          The id of the vendor that the expenses are related to
     * @param pageNum         The number of the page
     * @param pageSize        The size of the page
     * @return The view "list.html".
     */

    @GetMapping //ova e GET bidejki e listanje, nema promena vo baza, @GetMapping tuka znaci GET /, GET /expenses, prazen e bidejki imame @RequestMapping({"/", "/expenses"})
    public String listAll(@RequestParam(required = false) String title, //optional filter, ako ne e praten-null
                          @RequestParam(required = false) ExpenseCategory expenseCategory, //optional filter
                          @RequestParam(required = false) Long vendor, //optional filter
                          @RequestParam(defaultValue = "1") Integer pageNum, //ako nema parametar-str 1, pageNum e 1-based UI
                          @RequestParam(defaultValue = "10") Integer pageSize, //kolku redovi po stranica
                          Model model) {
        Page<Expense> page=expenseService.findPage(title, expenseCategory, vendor, pageNum-1, pageSize); //ja zema logikata od service, service raboti so 0-based zatoa pageNum-1
        model.addAttribute("expenses", page); //list.html kje koristi expenses, list.html kje pravi th:each="expense : ${expenses.content}"
        model.addAttribute("categories", ExpenseCategory.values()); //za dropdown filter
        model.addAttribute("vendors", vendorService.listAll()); //za dropdown filter
        return "list"; //se vrakja list.html
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/expenses/add'.
     *
     * @return The view "form.html".
     */

    @GetMapping("/add") // /expenses imame dodavame /add
    public String showAdd(Model model) { //ne kreira expense, ne pisuva vo baza, samo podgotvuva forma
        model.addAttribute("categories", ExpenseCategory.values()); //form.html ima <select name="expenseCategory">, pa mora da ima opcii
        model.addAttribute("vendors", vendorService.listAll()); //form.html ima <select name="vendor">, pa mora da ima vendors
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case, all 'input' elements should be filled with the appropriate value for the expenses that is updated.
     * The method should be mapped on path '/expenses/edit/[id]'.
     *
     * @return The view "form.html".
     */

    @GetMapping("/edit/{id}") //URL /expenses/edit/5, /expenses imame dodavame /edit/{id}
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.findById(id)); //form.html kje chita th:value="${expense.title}"
        model.addAttribute("categories", ExpenseCategory.values());
        model.addAttribute("vendors", vendorService.listAll());
        return "form";
    }

    /**
     * This method should create an expenses given the arguments it takes.
     * The method should be mapped on path '/expenses'.
     * After the expenses is created, all expenses should be displayed.
     *
     * @return The view "list.html".
     */

    @PostMapping() //POST /expenses, prazno bidejki vekje imame /expenses //add
    public String create(@RequestParam String title,
                         @RequestParam LocalDate dateCreated,
                         @RequestParam Double amount,
                         @RequestParam Integer daysToExpire,
                         @RequestParam ExpenseCategory expenseCategory,
                         @RequestParam Long vendor) {
        expenseService.create(title, dateCreated, amount, daysToExpire, expenseCategory, vendor); //controller samo prosleduva
        return "redirect:/expenses"; //POST->REDIRECT->GET, sprechuva dupli submit
    }

    /**
     * This method should update an expense given the arguments it takes.
     * The method should be mapped on path '/expenses/[id]'.
     * After the expense is updated, all expenses should be displayed.
     *
     * @return The view "list.html".
     */

    @PostMapping("/{id}") //POST /expense/5, /expenses imame dodavame /{id} //edit
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam LocalDate dateCreated,
                         @RequestParam Double amount,
                         @RequestParam Integer daysToExpire,
                         @RequestParam ExpenseCategory expenseCategory,
                         @RequestParam Long vendor) {
        expenseService.update(id, title, dateCreated, amount, daysToExpire, expenseCategory, vendor); //update vo baza
        return "redirect:/expenses";
    }

    /**
     * This method should delete the expense that has the appropriate identifier.
     * The method should be mapped on path '/expenses/delete/[id]'.
     * After the expenses is deleted, all expenses should be displayed.
     *
     * @return The view "list.html".
     */

    @PostMapping("/delete/{id}") //POST /expenses/delete/5, /expenses imame dodavame /delete/{id}
    public String delete(@PathVariable Long id) {
        expenseService.delete(id); //delete
        return "redirect:/expenses";
    }

    /**
     * This method should implement the logic for extending the expiration of an expense,
     * by adding one day to the daysToExpire.
     * The method should be mapped on path '/expenses/extend/[id]'.
     * After the operation, all expenses should be displayed.
     *
     * @return The view "list.html".
     */

    @PostMapping("/extend/{id}") //POST /expenses/extend/5, /expenses imame dodavame /extend/{id}
    public String extend(@PathVariable Long id) {
        expenseService.extendExpiration(id); //daysToExpire+1
        return "redirect:/expenses";
    }
}

