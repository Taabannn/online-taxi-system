package ir.maktab58.homework6.models.places;

/**
 * @author Taban Soleymani
 */
public enum Places {
    PLACE_A("A"),
    PLACE_B("B"),
    PLACE_C("C"),
    PLACE_D("D"),
    ;
    private String placeName;

    Places(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceName() {
        return placeName;
    }
}

