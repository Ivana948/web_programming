package mk.ukim.finki.wp.kol2025g2.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class SkiResort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "skiResort")
    private List<SkiSlope> skiSlopes;

    public SkiResort(String name, String location) {
        this.name = name;
        this.location = location;
    }
}
