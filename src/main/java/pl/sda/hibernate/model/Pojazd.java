package pl.sda.hibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Paweł Recław, AmeN
 * @project j133-zad-dom-pojazd
 * @created 27.11.2022
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pojazd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//- marka
    private String marka;

//- moc (double)
    private Double moc;

//- kolor
    private String kolor;

//- rok produkcji (nie data!)
    private Integer rokProdukcji;

//- elektryczny (boolean!)
    private boolean elektryczny;
}