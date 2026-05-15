package mk.ukim.finki.wp.kol2025g3.repository;

import mk.ukim.finki.wp.kol2025g3.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> { //dodavame samo extends JpaRepository<ENTITY, ID_TYPE>
    //so toa avtomatski gi dobivame metodite findById, findAll (go koristime za listAll) i save (za create)
}
