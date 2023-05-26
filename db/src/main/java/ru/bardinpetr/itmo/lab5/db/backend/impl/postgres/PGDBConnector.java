package ru.bardinpetr.itmo.lab5.db.backend.impl.postgres;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.ds.PGSimpleDataSource;
import ru.bardinpetr.itmo.lab5.db.auth.DBAuthProvider;
import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.InputStream;
import java.sql.SQLException;

@Slf4j
public class PGDBConnector {
    private final RowSetFactory rowSetFactory;
    private final DBAuthProvider dbAuthProvider;
    private final String connectUrl;

    public PGDBConnector(String connectUrl, DBAuthProvider dbAuthProvider) {
        this.connectUrl = connectUrl;
        this.dbAuthProvider = dbAuthProvider;

        try {
            rowSetFactory = RowSetProvider.newFactory();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize RowSet Factory");
        }
    }

    public PGSimpleDataSource getDataSource() throws DBCreateException {
        try {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUrl(connectUrl);
            ds.setUser(dbAuthProvider.getUsername());
            ds.setPassword(dbAuthProvider.getPassword());

            return ds;
        } catch (Exception e) {
            throw new DBCreateException("DB creation failed", e);
        }
    }

    public JdbcRowSet getRowSet() {
        try {
            JdbcRowSet rowSet = rowSetFactory.createJdbcRowSet();
            rowSet.setUrl(connectUrl);
            rowSet.setUsername(dbAuthProvider.getUsername());
            rowSet.setPassword(dbAuthProvider.getPassword());

            return rowSet;
        } catch (SQLException e) {
            throw new RuntimeException("Could not setup RowSet", e);
        }
    }

    /**
     * Runs sql script from input stream
     *
     * @return if script succeeded
     */
    public boolean bootstrap(InputStream startupFileStream) {
        try (var conn = getDataSource().getConnection()) {
            var sql = new String(startupFileStream.readAllBytes());
            conn.createStatement().executeUpdate(sql);
            return true;
        } catch (Exception ex) {
            log.error("Failed creation of db", ex);
            return false;
        }
    }
}
