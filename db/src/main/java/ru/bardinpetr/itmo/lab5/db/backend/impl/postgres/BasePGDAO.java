package ru.bardinpetr.itmo.lab5.db.backend.impl.postgres;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.ds.PGSimpleDataSource;
import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.bardinpetr.itmo.lab5.db.utils.RowSetUtils.rowSetStream;

/**
 * @param <V> primary key type
 * @param <T> row DTO type
 */
@Slf4j
public abstract class BasePGDAO<V, T> {

    protected final String tableName;
    protected final PGDBConnector connector;
    protected final PGSimpleDataSource datasource;
    protected final Connection connection;

    public BasePGDAO(PGDBConnector connector, String tableName) throws DBCreateException {
        this.connector = connector;
        this.tableName = tableName;


        try {
            this.datasource = connector.getDataSource();
            this.connection = datasource.getConnection();
        } catch (Exception e) {
            throw new DBCreateException(e);
        }
    }

    public abstract V insert(T data);

    public abstract boolean update(V id, T newData);

    public Collection<T> select() {
        try (var rs = connector.getRowSet()) {
            rs.setCommand("select * from %s".formatted(tableName));
            return rowSetStream(rs).map(i -> parseRow(rs)).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("select failed: ", ex);
        }
        return null;
    }

    public T select(V id) {
        try (var stmt = connection.prepareStatement("select * from %s where id=?".formatted(tableName))) {
            stmt.setInt(1, (Integer) id);
            var rowSet = stmt.executeQuery();
            if (!rowSet.next()) return null;
            return parseRow(rowSet);
        } catch (Exception ex) {
            log.error("select failed: ", ex);
        }
        return null;
    }

    public boolean delete(V id) {
        try (var stmt = connection.prepareStatement("delete from %s where id=?".formatted(tableName))) {
            stmt.setInt(1, (Integer) id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("delete failed: ", ex);
        }
        return false;
    }

    public boolean drop() {
        try {
            connection.createStatement().execute("""
                    DROP TABLE IF EXISTS %s CASCADE;
                    """.formatted(tableName));

            return true;
        } catch (SQLException e) {
            log.error("Can't drop", e);
            return false;
        }
    }

    public boolean truncate() {
        try {
            return connection.createStatement().execute("truncate table %s cascade".formatted(tableName));
        } catch (SQLException e) {
            log.error("Can't drop", e);
            return false;
        }
    }

    public abstract T parseRow(ResultSet rs);
}