package ru.bardinpetr.itmo.lab5.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.bardinpetr.itmo.lab5.models.data.Coordinates;
import ru.bardinpetr.itmo.lab5.models.data.Organization;
import ru.bardinpetr.itmo.lab5.models.data.OrganizationType;
import ru.bardinpetr.itmo.lab5.models.data.Worker;
import ru.bardinpetr.itmo.lab5.models.data.validation.WorkerValidator;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class WorkerValidationTest {

    private final Coordinates testCoords = new Coordinates(0, 0);
    private final Organization testOrg = new Organization(1, "", OrganizationType.COMMERCIAL);

    @DisplayName("Worker all-fields logic validator test")
    @Test
    void testWorkerValidation() {
        var testWorker = new Worker(
                0, "", ZonedDateTime.now(), 1, "", 0F, new Date(), null, testCoords, testOrg, null);
        var res = (new WorkerValidator()).validateAll(testWorker);
        assertNotNull(res);
        assertFalse(res.isAllowed(), "Empty worker names should not be allowed");
    }

    @DisplayName("Worker validation null field values test")
    @Test
    void testWorkerNullValidation() {
        assertThrows(
                NullPointerException.class,
                () -> new Worker(null, "", null, null, null, 0f, null, null, null, null, null),
                "Should not be able to create Worker object with null values in non-null fields");

        assertDoesNotThrow(
                () -> new Worker(0, "", ZonedDateTime.now(), 1, "", 0f, new Date(), null, testCoords, testOrg, null),
                "Worker could be created with nulls at endDate and position");
    }
}
