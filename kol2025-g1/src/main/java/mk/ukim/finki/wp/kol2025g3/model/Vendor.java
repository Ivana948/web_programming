package mk.ukim.finki.wp.kol2025g3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity //entitet
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id i generatedvalue
    private Long id;

    private String name;

    public Vendor(String name) {
        this.name = name;
    }

    //nema potreba od @OneToMany ako ne ni treba vo kodot
}
