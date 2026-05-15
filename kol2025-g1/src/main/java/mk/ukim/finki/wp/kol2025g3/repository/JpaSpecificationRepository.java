package mk.ukim.finki.wp.kol2025g3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.domain.Pageable;

@NoRepositoryBean //so ova kazuvame na Spring da NE pravi bean od ova repository, bidejki ova ne e konkretno repository tuku baza/template za dr repositories
public interface JpaSpecificationRepository<T, ID> extends JpaRepository<T, ID> { //T=tip na entitet (Expense, Vendor), ID=tip na primary key (Long), so extends toa se dobiva se od JpaRepository (findAll,findById,save,delete()...)
    Page<T> findAll(Specification<T> filter, Pageable pageable); //Specification<T>=filtri, Pageable=pagination, i vrakja Page<T>
}
