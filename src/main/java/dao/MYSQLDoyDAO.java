package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Doy;
import util.exceptions.DataSourceException;

public class MYSQLDoyDAO implements DoyDAO {

    public MYSQLDoyDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Doy> getAllDoys() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM doy ORDER BY name");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Doy> counties = new ArrayList<Doy>();

            while (rs.next()) {
                Doy county = new Doy();

                county.setId(rs.getString("id"));
                county.setName(rs.getString("name"));
                
                counties.add(county);
            }

            return counties;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String addDoy(String doy) throws DataSourceException {
        String result="unknown";
        
        try {

            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO doy(name) VALUES (?)"
            );
            
            pstmt.setString(1,  doy);

            pstmt.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            result="not ok";
        }
        
        return result;
    }
    
    @Override
    public String deleteDoy(String doy) throws DataSourceException{
        
        String result = "unknown";
        
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "DELETE FROM doy WHERE name=?"
            );

            pstmt.setString(1, doy);
            pstmt.executeUpdate();

            result = "delete ok";
        } catch (Exception ex) {
            result = "not ok";
        }
        
        return result;
    }

    private final Connection db_conn;

}