package mk.ukim.finki.wp.kol2025g2.repository;

import mk.ukim.finki.wp.kol2025g2.model.SkiResort;
import mk.ukim.finki.wp.kol2025g2.model.SkiSlope;
import mk.ukim.finki.wp.kol2025g2.model.SlopeDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SkiSlopeRepository extends JpaRepository<SkiSlope, Long> {
    List<SkiSlope> filter(
            @RequestParam String name,
            @RequestParam Integer length,
            @RequestParam SlopeDifficulty difficulty,
            @RequestParam SkiResort skiResort);
}
