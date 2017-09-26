package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Log;
import util.exceptions.DataSourceException;
import util.EnvironmentVariables;

public class MYSQLLogDAO implements LogDAO {

    public MYSQLLogDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Log> getLog(String fromdate, String todate) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT * FROM log "
                    + "WHERE STR_TO_DATE(date,'%d/%m/%Y') >= STR_TO_DATE(?,'%d/%m/%Y') AND "
                    + "STR_TO_DATE(date,'%d/%m/%Y') <= STR_TO_DATE(?,'%d/%m/%Y')");

            pstmt.setString(1, fromdate);
            pstmt.setString(2, todate);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Log> logs = new ArrayList<Log>();

            while (rs.next()) {
                Log log = new Log();
                
                log.setId(rs.getString("id"));
                log.setUsername(rs.getString("username"));
                log.setDate(rs.getString("date"));
                log.setTime(rs.getString("time"));
                log.setAction(rs.getString("action"));
                
                logs.add(log);
            }

            return logs;
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public boolean insertLog(String username, String date, String time, String action) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO log(username, date, time, action) VALUES (?, ?, ?, ?)"
            );
            
            pstmt.setString(1,  username);
            pstmt.setString(2,  date);
            pstmt.setString(3,  time);
            pstmt.setString(4,  action);
            
            pstmt.executeUpdate();

            return true;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

    private final Connection db_conn;

}
