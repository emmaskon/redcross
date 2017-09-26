package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.exceptions.DataSourceException;
import models.User;
import util.EnvironmentVariables;

public class MYSQLUserDAO implements UserDAO {

    public MYSQLUserDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public User getUser(String username, String password) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT username, role, state_id, city_id FROM users WHERE username=? AND password = SHA1(?)"
            );

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            User user = new User();
            user.setUsername(username);
            user.setRole(rs.getString("role"));
            user.setState_id(rs.getString("state_id"));
            user.setCity_id(rs.getString("city_id"));
            return user;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String getUsername(String username) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT username FROM users WHERE username=?"
            );

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return "not_available";
            }

            return rs.getString("username");

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

    @Override
    public boolean createUser(String username, String password) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO users(username, password) VALUES (?, SHA(?))"
            );

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            if (ex.getErrorCode() == EnvironmentVariables.UsefulMYSQLErrorCodes.DUPLICATE_ENTRY) {
                return false;
            } else {
                throw new DataSourceException(ex);
            }
        }
    }

    @Override
    public boolean updateUser(String username, String new_password) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "UPDATE users SET password=SHA(?) WHERE username=?"
            );

            pstmt.setString(1, new_password);
            pstmt.setString(2, username);

            pstmt.executeUpdate();
            
            return  true;

        } catch (SQLException ex) {
            throw new DataSourceException(ex);
        }
    }

    private final Connection db_conn;

}
