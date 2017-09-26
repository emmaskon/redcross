package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Municipality;
import util.exceptions.DataSourceException;

public class MYSQLMunicipalityDAO implements MunicipalityDAO {

    public MYSQLMunicipalityDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Municipality> getAllMunicipalities() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM municipalities ORDER BY name");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Municipality> municipalities = new ArrayList<Municipality>();

            while (rs.next()) {
                Municipality municipality = new Municipality();

                municipality.setName(rs.getString("name"));
                
                municipalities.add(municipality);
            }

            return municipalities;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String addMunicipality(String municipality_name) throws DataSourceException {
        String result="unknown";
        
        try {

            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO municipalities(name) VALUES (?)"
            );
            
            pstmt.setString(1,  municipality_name);

            pstmt.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            result="not ok";
        }
        
        return result;
    }
    
    @Override
    public String deleteMunicipality(String municipality_name) throws DataSourceException{
        
        String result = "unknown";
        
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "DELETE FROM municipalities WHERE name=?"
            );

            pstmt.setString(1, municipality_name);
            pstmt.executeUpdate();

            result = "delete ok";
        } catch (Exception ex) {
            result = "not ok";
        }
        
        return result;
    }

    private final Connection db_conn;

}