package ru.bardinpetr.itmo.lab5.models.data.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionInfo {
    private String name;
    private String type;
    private ZonedDateTime initializationDate;
    private Integer itemsCount;
}
