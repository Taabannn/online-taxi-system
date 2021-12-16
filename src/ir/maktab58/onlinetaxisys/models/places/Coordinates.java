package ir.maktab58.onlinetaxisys.models.places;

import lombok.*;

import javax.persistence.Entity;

/**
 * @author Taban Soleymani
 */
@Entity
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Coordinates {
    int x;
    int y;

    @Builder(setterPrefix = "with")
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
