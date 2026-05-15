package mk.ukim.finki.wp.kol2025g2.service.impl;

import mk.ukim.finki.wp.kol2025g2.model.SkiResort;
import mk.ukim.finki.wp.kol2025g2.model.SkiSlope;
import mk.ukim.finki.wp.kol2025g2.model.SlopeDifficulty;
import mk.ukim.finki.wp.kol2025g2.model.exceptions.InvalidSkiSlopeIdException;
import mk.ukim.finki.wp.kol2025g2.repository.SkiSlopeRepository;
import mk.ukim.finki.wp.kol2025g2.service.SkiResortService;
import mk.ukim.finki.wp.kol2025g2.service.SkiSlopeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkiSlopeServiceImpl implements SkiSlopeService {
    private final SkiSlopeRepository skiSlopeRepository;
    private final SkiResortService skiResortService;

    public SkiSlopeServiceImpl(SkiSlopeRepository skiSlopeRepository, SkiResortService skiResortService) {
        this.skiSlopeRepository = skiSlopeRepository;
        this.skiResortService = skiResortService;
    }

    @Override
    public List<SkiSlope> listAll() {
        return skiSlopeRepository.findAll();
    }

    @Override
    public SkiSlope findById(Long id) {
        return skiSlopeRepository.findById(id).orElseThrow(InvalidSkiSlopeIdException::new);
    }

    @Override
    public SkiSlope create(String name, Integer length, SlopeDifficulty difficulty, Long skiResort) {
        SkiSlope skiSlope = new SkiSlope(name, length, difficulty, skiResortService.findById(skiResort));
        return skiSlopeRepository.save(skiSlope);
    }

    @Override
    public SkiSlope update(Long id, String name, Integer length, SlopeDifficulty difficulty, Long skiResort) {
        SkiSlope skiSlope=findById(id);
        skiSlope.setName(name);
        skiSlope.setLength(length);
        skiSlope.setDifficulty(difficulty);
        skiSlope.setSkiResort(skiResortService.findById(skiResort));
        return skiSlopeRepository.save(skiSlope);
    }

    @Override
    public SkiSlope delete(Long id) {
        SkiSlope skiSlope=findById(id);
        skiSlopeRepository.delete(skiSlope);
        return skiSlope;
    }

    @Override
    public SkiSlope close(Long id) {
        SkiSlope skiSlope=findById(id);
        skiSlope.setClosed(true);
        return skiSlopeRepository.save(skiSlope);
    }

    @Override
    public Page<SkiSlope> findPage(String name, Integer length, SlopeDifficulty difficulty, Long skiResort, int pageNum, int pageSize) {
        SkiResort resort=skiResort != null ? skiResortService.findById(skiResort) : null;

        List<SkiSlope> filtered=skiSlopeRepository.filter(name, length, difficulty, resort);

        int start=Math.min(pageNum*pageSize, filtered.size());
        int end=Math.min(start+pageSize, filtered.size());

        return new PageImpl<>(
                filtered.subList(start, end),
                PageRequest.of(pageNum, pageSize),
                filtered.size()
        );
    }
}
