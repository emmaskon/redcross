package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Job;
import util.exceptions.DataSourceException;

public class MYSQLJobDAO implements JobDAO {

    public MYSQLJobDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Job> getAllJobs() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM jobs ORDER BY name");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Job> jobs = new ArrayList<Job>();

            while (rs.next()) {
                Job job = new Job();

                job.setName(rs.getString("name"));
                
                jobs.add(job);
            }

            return jobs;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String addJob(String job) throws DataSourceException {
        String result="unknown";
        
        try {

            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO jobs(name) VALUES (?)"
            );
            
            pstmt.setString(1,  job);

            pstmt.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            result="not ok";
        }
        
        return result;
    }
    
    @Override
    public String deleteJob(String job) throws DataSourceException{
        
        String result = "unknown";
        
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "DELETE FROM jobs WHERE name=?"
            );

            pstmt.setString(1, job);
            pstmt.executeUpdate();

            result = "delete ok";
        } catch (Exception ex) {
            result = "not ok";
        }
        
        return result;
    }

    private final Connection db_conn;

}