package mk.ukim.finki.wp.kol2025g3.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2025g3.model.Vendor;
import mk.ukim.finki.wp.kol2025g3.repository.VendorRepository;
import mk.ukim.finki.wp.kol2025g3.service.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    @Override
    public Vendor findById(Long id) {
        return vendorRepository.findById(id).get();
    }

    @Override
    public List<Vendor> listAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor create(String name) {
        return vendorRepository.save(new Vendor(name));
    }
}
