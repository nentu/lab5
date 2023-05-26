package ru.bardinpetr.itmo.lab5.db.utils;

import javax.sql.RowSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class RowSetUtils {
    public static Stream<?> rowSetStream(RowSet rs) throws SQLException {
        rs.execute();
        return Stream.generate(() -> {
            try {
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).takeWhile(i -> i);
    }
}
