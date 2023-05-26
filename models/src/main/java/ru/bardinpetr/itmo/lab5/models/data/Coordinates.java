package ru.bardinpetr.itmo.lab5.models.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.models.data.annotation.FieldValidator;
import ru.bardinpetr.itmo.lab5.models.data.annotation.InteractText;
import ru.bardinpetr.itmo.lab5.models.data.validation.CoordinatesValidator;

//import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class Coordinates implements Comparable<Coordinates> {
    @NonNull
    @InteractText("Enter x coordinate")
    @FieldValidator(CoordinatesValidator.class)
    private Integer x;

    @InteractText("Enter y coordinate")
    @FieldValidator(CoordinatesValidator.class)
    private float y;

    public Coordinates() {
    }

    public static Coordinates fromString(String str) {
        if (str == null) return null;

        str = str.substring(1, str.length() - 1);
        var parse = str.split(",");
        return new Coordinates(
                Integer.valueOf(parse[0]),
                Float.parseFloat(parse[1]));

    }

    /**
     * @param other the object to be compared.
     * @return {@literal <} 0 than other object is greater, {@literal >} 0 this object is greater
     */
    @Override
    public int compareTo(Coordinates other) {
        return (int) ((x + y) - (other.x + other.y));
    }

}
