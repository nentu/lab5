package ru.bardinpetr.itmo.lab5.db.frontend.adapters.sync;

import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionDAO;
import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionReadDAO;
import ru.bardinpetr.itmo.lab5.db.frontend.dao.ICollectionWriteDAO;
import ru.bardinpetr.itmo.lab5.models.data.collection.IKeyedEntity;
import ru.bardinpetr.itmo.lab5.models.data.collection.IOwnedEntity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class SynchronizedDAOInvocationHandler<K, V extends IKeyedEntity<K> & IOwnedEntity> implements InvocationHandler {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final ICollectionDAO<K, V> target;
    private final Set<String> readMethodNames;
    private final Set<String> writeMethodNames;
    private final Set<String> blacklistMethodNames;

    public SynchronizedDAOInvocationHandler(ICollectionDAO<K, V> target) {
        this.target = target;

        readMethodNames = getInterfaceMethodsNames(ICollectionReadDAO.class);
        writeMethodNames = getInterfaceMethodsNames(ICollectionWriteDAO.class);
        blacklistMethodNames = Set.of();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var methodName = method.getName();
        if (readMethodNames.contains(methodName))
            return invokeLocked(method, args, lock.readLock());
        else if (writeMethodNames.contains(methodName))
            return invokeLocked(method, args, lock.writeLock());
        else if (blacklistMethodNames.contains(methodName))
            throw new NoSuchMethodException();
        else
            return method.invoke(target, args);
    }

    private Object invokeLocked(Method original, Object[] args, Lock lock) throws Throwable {
        lock.lock();
        var result = original.invoke(target, args);
        lock.unlock();
        return result;
    }

    private Set<String> getInterfaceMethodsNames(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());
    }
}
