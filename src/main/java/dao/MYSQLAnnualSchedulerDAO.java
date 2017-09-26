package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Annual_Scheduler;
import util.exceptions.DataSourceException;

public class MYSQLAnnualSchedulerDAO implements AnnualSchedulerDAO {

    public MYSQLAnnualSchedulerDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public Annual_Scheduler getAnnualScheduler() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT * FROM annual_scheduler"
            );

            ResultSet rs = pstmt.executeQuery();

            Annual_Scheduler annual_scheduler = new Annual_Scheduler();

            while (rs.next()) {
                annual_scheduler.setYear(rs.getString("year"));
            }
            
            return annual_scheduler;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String updateAnnualScheduler(int current_year, int pre_year) throws DataSourceException {
        
        String result = "unknown";
        
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("UPDATE annual_scheduler SET year=? "
                                                                + "WHERE year=?");
            pstmt.setInt(1, current_year);
            pstmt.setInt(2, pre_year);
                    
            pstmt.executeUpdate();
            
            result = "insert ok";
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
        
        return result;
    }

    private final Connection db_conn;

}