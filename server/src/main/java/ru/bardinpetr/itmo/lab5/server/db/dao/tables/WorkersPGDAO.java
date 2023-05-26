package ru.bardinpetr.itmo.lab5.server.db.dao.tables;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.BasePGDAO;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.PGDBConnector;
import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;
import ru.bardinpetr.itmo.lab5.models.data.Coordinates;
import ru.bardinpetr.itmo.lab5.models.data.Position;
import ru.bardinpetr.itmo.lab5.server.db.dto.WorkerDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.bardinpetr.itmo.lab5.db.utils.RowSetUtils.rowSetStream;

@Slf4j
public class WorkersPGDAO extends BasePGDAO<Integer, WorkerDTO> {

    public WorkersPGDAO(PGDBConnector connector) throws DBCreateException {
        super(connector, "worker");
    }

    private void setPreparedStatement(PreparedStatement s, WorkerDTO data) throws SQLException {
        s.setInt(1, data.ownerId());

        if (data.organizationId() != null)
            s.setInt(2, data.organizationId());
        else
            s.setNull(2, Types.INTEGER);

        s.setString(3, data.name());
        s.setFloat(4, data.salary());
        s.setDate(5, new Date(data.startDate().getTime()));

        if (data.endDate() != null)
            s.setDate(6, Date.valueOf(data.endDate()));
        else
            s.setNull(6, Types.DATE);

        s.setString(7, "(%s, %s)".formatted(data.coordinates().getX(), data.coordinates().getY()));

        if (data.position() != null)
            s.setString(8, data.position().name());
        else
            s.setNull(8, Types.VARCHAR);
    }

    @Override
    public Collection<WorkerDTO> select() {
        try (var rs = connector.getRowSet()) {
            rs.setCommand("""
                    select *
                    from worker
                    join users on worker.ownerId = users.id
                    """);
            return rowSetStream(rs).map(i -> parseRow(rs)).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("select failed: ", ex);
        }
        return null;
    }

    @Override
    public WorkerDTO select(Integer id) {
        try (var stmt = connection.prepareStatement("""
                select *
                from worker join users on worker.ownerId = users.id
                where worker.id=?
                """)) {
            stmt.setInt(1, id);
            var rowSet = stmt.executeQuery();
            if (!rowSet.next()) return null;
            return parseRow(rowSet);
        } catch (Exception ex) {
            log.error("select failed: ", ex);
        }
        return null;
    }

    @Override
    public Integer insert(WorkerDTO data) {
        try {
            var s = connection.prepareStatement(
                    """
                            INSERT INTO worker
                            VALUES
                            	(default,default, ?, ?, ?, ?, ?, ?, ?::worker_coordinates, ?::worker_position) returning id
                            """
            );
            setPreparedStatement(s, data);
            var rs = s.executeQuery();
            if (!rs.next())
                return null;
            return rs.getInt("id");
        } catch (SQLException e) {
            log.error("Can't insert into users table", e);
            return null;
        }
    }


    @Override
    public boolean update(Integer id, WorkerDTO newData) {
        try {
            var s = connection.prepareStatement(
                    """
                            UPDATE worker
                            SET     ownerId= ?,
                                    organizationID= ?,
                                    name= ?,
                                    salary= ?,
                                    startDate= ?,
                                    endDate= ?,
                                    coordinates= ?::worker_coordinates,
                                    position= ?::worker_position

                            WHERE id = ?;
                            """
            );
            setPreparedStatement(s, newData);
            s.setInt(9, id);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Can't insert into users table", e);
            return false;
        }
    }

    @Override
    public WorkerDTO parseRow(ResultSet rs) {
        try {
            LocalDate endDate;
            if (rs.getTimestamp("endDate") != null) {
                endDate = rs.getTimestamp("endDate").toLocalDateTime().toLocalDate();
            } else endDate = null;

            var x = rs.getString("position");

            return new WorkerDTO(
                    rs.getInt("id"),
                    rs.getInt("ownerId"),
                    rs.getInt("organizationId"),
                    rs.getTimestamp("creationDate").toInstant().atZone(ZoneId.systemDefault()),
                    rs.getTimestamp("startDate"),
                    endDate,
                    rs.getString("name"),
                    rs.getFloat("salary"),
                    Coordinates.fromString(rs.getString("coordinates")),
                    x == null ? null : Position.valueOf(x),
                    rs.getString("login"));
        } catch (SQLException e) {
            log.error("row parse failed at {}", tableName, e);
            return null;
        }
    }
}
