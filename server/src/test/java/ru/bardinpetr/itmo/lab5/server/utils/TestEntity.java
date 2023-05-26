package ru.bardinpetr.itmo.lab5.server.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;

@Data
public class TestEntity implements IKeyedEntity<Integer>, Comparable<TestEntity> {

    @NonNull
    private Integer id;

    public TestEntity() {
    }

    public TestEntity(Integer id) {
        this.id = id;
    }

    public static TestEntity create() {
        return new TestEntity((int) (Math.random() * 100000));
    }

    @JsonIgnore
    @Override
    public Integer getPrimaryKey() {
        return id;
    }

    @Override
    public int compareTo(TestEntity o) {
        return id.compareTo(o.id);
    }
}
