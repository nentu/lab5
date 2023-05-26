package ru.bardinpetr.itmo.lab5.server.db.dao.tables;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.BasePGDAO;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.PGDBConnector;
import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;
import ru.bardinpetr.itmo.lab5.models.data.OrganizationType;
import ru.bardinpetr.itmo.lab5.server.db.dto.OrganizationDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Slf4j
public class OrganizationsPGDAO extends BasePGDAO<Integer, OrganizationDTO> {
    public OrganizationsPGDAO(PGDBConnector connector) throws DBCreateException {
        super(connector, "organization");

    }

    @Override
    public Integer insert(OrganizationDTO data) {
        try (var stmt = connection.prepareStatement("insert into organization values (default, ?, ?::organization_type) returning id")) {
            setPreparedStatement(stmt, data);
            var rs = stmt.executeQuery();
            if (!rs.next())
                return null;
            return rs.getInt("id");
        } catch (Exception ex) {
            log.error("insert failed: ", ex);
        }
        return null;
    }

    private void setPreparedStatement(PreparedStatement s, OrganizationDTO data) throws SQLException {
        if (data.fullName() != null)
            s.setString(1, data.fullName());
        else
            s.setNull(1, Types.VARCHAR);

        if (data.type() != null)
            s.setString(2, data.type().name());
        else
            s.setNull(2, Types.VARCHAR);
    }

    @Override
    public boolean update(Integer id, OrganizationDTO newData) {
        try {
            var s = connection.prepareStatement(
                    """
                            UPDATE organization
                            SET     fullName= ?,
                                    type= ?::organization_type
                            WHERE id = ?;
                            """
            );
            setPreparedStatement(s, newData);
            s.setInt(3, id);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Can't insert into users table", e);
            return false;
        }


    }

    @Override
    public OrganizationDTO parseRow(ResultSet rs) {
        try {
            return new OrganizationDTO(rs.getInt("id"), rs.getString("fullName"), OrganizationType.valueOf(rs.getString("type")));
        } catch (SQLException e) {
            log.error("row parse failed at {}", tableName, e);
            return null;
        }
    }
}
