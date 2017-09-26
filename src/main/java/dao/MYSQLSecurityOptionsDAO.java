package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.exceptions.DataSourceException;

public class MYSQLSecurityOptionsDAO implements SecurityOptionsDAO {

    public MYSQLSecurityOptionsDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public boolean isRecaptchaEnabled() throws DataSourceException {

        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT recaptcha_enabled FROM securityoptions"
            );
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return true; //By default is true
            }

            return (rs.getString("recaptcha_enabled").equals("true"));
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }

    }

    private final Connection db_conn;
}
