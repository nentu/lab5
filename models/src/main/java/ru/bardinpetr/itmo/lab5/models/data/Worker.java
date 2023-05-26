package ru.bardinpetr.itmo.lab5.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import ru.bardinpetr.itmo.lab5.models.data.annotation.FieldValidator;
import ru.bardinpetr.itmo.lab5.models.data.annotation.InputNullable;
import ru.bardinpetr.itmo.lab5.models.data.annotation.InteractText;
import ru.bardinpetr.itmo.lab5.models.data.annotation.NotPromptRequired;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;
import ru.bardinpetr.itmo.lab5.models.data.collection.IOwnedEntity;
import ru.bardinpetr.itmo.lab5.models.data.validation.WorkerValidator;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Data
@AllArgsConstructor
public class Worker implements Comparable<Worker>, IKeyedEntity<Integer>, IOwnedEntity {
    @With
    @NonNull
    @NotPromptRequired
    private final Integer id;
    @With
    @NotPromptRequired
    private String ownerUsername = "";

    @With
    @NonNull
    @NotPromptRequired
    private final java.time.ZonedDateTime creationDate;

    @With
    @NotPromptRequired
    private Integer owner;

    @NonNull
    @InteractText("Enter a name")
    @FieldValidator(WorkerValidator.class)
    private String name;

    @InteractText("Enter salary")
    @FieldValidator(WorkerValidator.class)
    private float salary;

    @NonNull
    @InteractText("Enter start date in DD-MM-YYYY format")
    @FieldValidator(WorkerValidator.class)
    private java.util.Date startDate;

    @InteractText("Enter end date in DD-MM-YYYY format")
    @FieldValidator(WorkerValidator.class)
    @InputNullable
    private java.time.LocalDate endDate;

    @NonNull
    @InteractText("Enter coordinates")
    @FieldValidator(WorkerValidator.class)
    private Coordinates coordinates;

    @InteractText("Enter an organization")
    @FieldValidator(WorkerValidator.class)
    @InputNullable
    private Organization organization;


    @InteractText("""
            Enter position from list:
                ENGINEER,
                HEAD_OF_DEPARTMENT,
                LEAD_DEVELOPER,
                CLEANER,
                MANAGER_OF_CLEANING""")
    @FieldValidator(WorkerValidator.class)
    @InputNullable
    private Position position;

    public Worker() {
        id = 0;
        creationDate = ZonedDateTime.now();
    }


    public static Comparator<Worker> getComparator() {
        return Comparator
                .comparing(Worker::getOrganization, nullsLast(naturalOrder()))
                .thenComparing(Worker::getName)
                .thenComparing(Worker::getPosition, nullsLast(naturalOrder()))
                .thenComparing(Worker::getSalary)
                .thenComparing((x, y) -> -1 * x.startDate.compareTo(y.startDate))
                .thenComparing(Worker::getEndDate, nullsLast(naturalOrder()))
                .thenComparing(Worker::getCoordinates)
                .thenComparing(Worker::getCreationDate)
                .thenComparing(Worker::getId);
    }

    public static String nicePrintFormat(List<Worker> list) {
        String s = "";
        for (var i : list) {
            String coordinates = "(x: " + i.getCoordinates().getX() + "," +
                    "y: " + i.getCoordinates().getX() + ')';
            String organization = "";
            if (i.getOrganization() != null) {
                organization = String.format(
                        "(full name: %s, type: %s)",
                        i.getOrganization().getFullName(),
                        i.getOrganization().getType()
                );
            } else organization = "null";
            s += "\n\tid: " + i.getId() +
                    ",\n\t creationDate: " + i.getCreationDate() +
                    ",\n\t owner: " + i.getOwnerUsername() +
                    ",\n\t name: '" + i.getName() + '\'' +
                    ",\n\t coordinates: " + coordinates +
                    ",\n\t salary: " + i.getSalary() +
                    ",\n\t startDate: " + i.getStartDate() +
                    ",\n\t organization: " + organization +
                    ",\n\t endDate: " + i.getEndDate() +
                    ",\n\t position: " + i.getPosition();
        }
        return s;

    }

    @JsonIgnore
    @Override
    public Integer getPrimaryKey() {
        return id;
    }

    /**
     * @param worker the object to be compared.
     */
    public int compareTo(@NonNull Worker worker) {
        return getComparator().compare(this, worker);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        if (Float.compare(worker.getSalary(), getSalary()) != 0) return false;
        if (!getId().equals(worker.getId())) return false;
        if (!getName().equals(worker.getName())) return false;
        if (!getCoordinates().equals(worker.getCoordinates())) return false;
        if (!getStartDate().equals(worker.getStartDate())) return false;
        if (getOrganization() != null ? !getOrganization().equals(worker.getOrganization()) : worker.getOrganization() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(worker.getEndDate()) : worker.getEndDate() != null)
            return false;
        return getPosition() == worker.getPosition();
    }

    @Override
    public Integer getOwner() {
        return owner;
    }

    @Override
    public boolean setOwner(Integer owner) {
        if (this.owner != null) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }
}