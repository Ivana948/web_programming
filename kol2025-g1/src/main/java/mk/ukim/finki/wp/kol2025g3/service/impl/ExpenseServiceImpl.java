package mk.ukim.finki.wp.kol2025g3.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2025g3.model.Expense;
import mk.ukim.finki.wp.kol2025g3.model.ExpenseCategory;
import mk.ukim.finki.wp.kol2025g3.repository.ExpenseRepository;
import mk.ukim.finki.wp.kol2025g3.service.ExpenseService;
import mk.ukim.finki.wp.kol2025g3.service.VendorService;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

import static mk.ukim.finki.wp.kol2025g3.service.FieldFilterSpecification.*;

@Service //biznis logika, bez ova Spring nema da ja najde klasata
@AllArgsConstructor //konstruktor
public class ExpenseServiceImpl implements ExpenseService { //mora da gi implementirame site metodi od ExpenseService
    private final ExpenseRepository expenseRepository; //sekoj service raboti so svojot repository,
    private final VendorService vendorService; //a so drugi entiteti komunicira preku nivni service-i !


    @Override
    public List<Expense> listAll() {
        return expenseRepository.findAll(); //expenseRepository ima metod findAll()
    }

    @Override
    public Expense findById(Long id) {
        return expenseRepository.findById(id).get(); //bara expense po id, ako nema-exception, findById vrakja Optional, .get() frla exception avtomatski
    }

    @Override
    public Expense create(String title, LocalDate dateCreated, Double amount, Integer daysToExpire, ExpenseCategory expenseCategory, Long vendorId) {
        return expenseRepository.save(new Expense(title, dateCreated, amount, daysToExpire, expenseCategory, vendorService.findById(vendorId))); //pravime nov expense, za vendorId prvin go naogjame ako ne postoi exception potoa save
    }

    @Override
    public Expense update(Long id, String title, LocalDate dateCreated, Double amount, Integer daysToExpire, ExpenseCategory expenseCategory, Long vendorId) {
        Expense e=findById(id); //prvin naogjame expense
        e.setTitle(title); //gi setirame site polinja
        e.setDateCreated(dateCreated);
        e.setAmount(amount);
        e.setDaysToExpire(daysToExpire);
        e.setExpenseCategory(expenseCategory);
        e.setVendor(vendorService.findById(vendorId)); //naogjame vendor
        expenseRepository.save(e); //save
        return e; //ne pravime nov expense tuku modificirame postoechki
    }

    @Override
    public Expense delete(Long id) {
        Expense e=findById(id); //naogjame expense
        expenseRepository.delete(e); //delete
        return e; //go vrakjame
    }

    @Override
    public Expense extendExpiration(Long id) {
        Expense e=findById(id); //naogjame expense
        e.setDaysToExpire(e.getDaysToExpire()+1); //daysToExpire+1
        expenseRepository.save(e); //save
        return e; //go vrakjame

    }

    @Override
    public Page<Expense> findPage(String title, ExpenseCategory expenseCategory, Long vendor, int pageNum, int pageSize) {
        Specification<Expense> specification=Specification.allOf( //sekoj filter vrakja uslov ili null, .allOf-gi spojuva, null se ignoriraat
                filterContainsText(Expense.class, "title", title),
                filterEqualsV(Expense.class, "expenseCategory", expenseCategory),
                filterEquals(Expense.class, "vendor.id", vendor)
        );

        return this.expenseRepository.findAll(
                specification,
                PageRequest.of(pageNum, pageSize)
        );
    }
}
