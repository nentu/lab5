package ru.bardinpetr.itmo.lab5.models.data.collection;

import ru.bardinpetr.itmo.lab5.models.data.Worker;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class WorkerCollection extends TreeSet<Worker> implements ISetCollection<Integer, Worker> {
    @Override
    public boolean add(Worker worker) {
        if (stream().anyMatch(i -> i.getId().equals(worker.getId())))
            throw new IllegalArgumentException("Id should be unique");
        return super.add(worker);
    }

    @Override
    public boolean addAll(Collection<? extends Worker> workers) {
        Set<Integer> idSet = workers.stream().map(Worker::getId).collect(Collectors.toSet());
        if (idSet.size() != workers.size() || stream().anyMatch(i -> idSet.contains(i.getId())))
            throw new IllegalArgumentException("Id should be unique");
        return super.addAll(workers);
    }
}
