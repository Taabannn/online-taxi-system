package ir.maktab58.onlinetaxisys.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Taban Soleymani
 */
@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    int x;
    int y;

    @Builder(setterPrefix = "with")
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
