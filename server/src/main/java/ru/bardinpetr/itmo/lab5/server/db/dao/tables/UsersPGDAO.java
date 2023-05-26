package ru.bardinpetr.itmo.lab5.server.db.dao.tables;

import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.BasePGDAO;
import ru.bardinpetr.itmo.lab5.db.backend.impl.postgres.PGDBConnector;
import ru.bardinpetr.itmo.lab5.db.errors.DBCreateException;
import ru.bardinpetr.itmo.lab5.server.db.dao.exception.OverLimitedUsername;
import ru.bardinpetr.itmo.lab5.server.db.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class UsersPGDAO extends BasePGDAO<Integer, UserDTO> {
    public UsersPGDAO(PGDBConnector connector) throws DBCreateException {
        super(connector, "users");
    }

//    public boolean createTable() {
//        try {
//            var t = connection.prepareStatement(
//                    """
//                            CREATE TABLE users
//                            (
//                                id int generated always as identity PRIMARY KEY,
//                                login varchar(50) UNIQUE NOT NULL,
//                                password bytea NOT NULL,
//                                salt varchar(10) NOT NULL
//                            );
//                            """
//            );
//            return t.executeUpdate() > 0;
//        } catch (SQLException e) {
//            log.error("Can't create users table", e);
//            return false;
//        }
//    }

    @Override
    public Integer insert(UserDTO data) {
        if (!data.validate()) return null;
        try {
            var s = connection.prepareStatement(
                    """
                            INSERT INTO users(login, password, salt)
                            VALUES
                            	(?, ?, ?) returning id
                            """
            );
            s.setString(1, data.getUsername());
            s.setBytes(2, data.getHashedPassword());
            s.setString(3, data.getSalt());
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
    public boolean update(Integer id, UserDTO newData) {
        if (!newData.validate()) return false;
        try {
            var s = connection.prepareStatement(
                    """
                            UPDATE users
                            SET login = ?,
                                password= ?,
                                salt= ?
                            WHERE id = ?;
                            """
            );
            s.setString(1, newData.getUsername());
            s.setBytes(2, newData.getHashedPassword());
            s.setString(3, newData.getSalt());
            s.setInt(4, id);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Can't insert into users table", e);
            return false;
        }
    }

    @Override
    public Collection<UserDTO> select() {
        try {
            var st = connection.createStatement();
            var s = st.executeQuery("""
                    SELECT * FROM users;
                    """);
            List<UserDTO> res = new ArrayList<>();
            while (s.next()) {
                res.add(new UserDTO(
                        s.getInt("id"),
                        s.getString("login"),
                        s.getBytes("password"),
                        s.getString("salt")
                ));
            }
            return res;
        } catch (SQLException e) {
            log.error("Can't select all data", e);
            return null;
        }
    }

    public UserDTO selectByUsername(String username) throws OverLimitedUsername {
        if (username.length() > 50) {
            System.out.println("Username is overloaded");
            throw new OverLimitedUsername();
        }
        try {
            var st = connection.prepareStatement("""
                    SELECT * FROM users
                    WHERE login = ?;
                    """);
            st.setString(1, username);
            var s = st.executeQuery();

            if (s.next())
                return new UserDTO(
                        s.getInt("id"),
                        s.getString("login"),
                        s.getBytes("password"),
                        s.getString("salt")
                );
            else return null;
        } catch (SQLException e) {
            log.error("Can't select all data", e);
            return null;
        }
    }


    @Override
    public UserDTO select(Integer id) {
        try {
            var st = connection.prepareStatement("""
                    SELECT * FROM users
                    WHERE id = ?;
                    """);
            st.setInt(1, id);
            var s = st.executeQuery();
            s.next();
            return new UserDTO(
                    s.getInt("id"),
                    s.getString("login"),
                    s.getBytes("password"),
                    s.getString("salt")
            );
        } catch (SQLException e) {
            log.error("Can't select all data", e);
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try {
            var t = connection.prepareStatement("""
                    DELETE FROM users
                    WHERE id = ?;
                    """);
            t.setInt(1, id);
            return t.execute();
        } catch (SQLException e) {
            log.error("Can't drop", e);
            return false;
        }
    }

    @Override
    public UserDTO parseRow(ResultSet rs) {
        try {
            if (rs.next())
                return new UserDTO(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getBytes("password"),
                        rs.getString("salt")
                );
            else return null;
        } catch (SQLException e) {
            log.error("Can't parse row");
            return null;
        }
    }
}
