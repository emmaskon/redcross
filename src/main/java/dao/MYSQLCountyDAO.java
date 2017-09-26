package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.County;
import util.exceptions.DataSourceException;

public class MYSQLCountyDAO implements CountyDAO {

    public MYSQLCountyDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<County> getAllCounties() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM counties ORDER BY name");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<County> counties = new ArrayList<County>();

            while (rs.next()) {
                County county = new County();

                county.setState(rs.getString("state"));
                county.setName(rs.getString("name"));
                
                counties.add(county);
            }

            return counties;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<County> getCounty(String name) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM counties WHERE name=? ORDER BY name");

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<County> counties = new ArrayList<County>();

            while (rs.next()) {
                County county = new County();

                county.setState(rs.getString("state"));
                county.setName(rs.getString("name"));
                
                counties.add(county);
            }

            return counties;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<County> getCountiesForState(String state_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM counties WHERE state=? ORDER BY name");

            pstmt.setString(1, state_id);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<County> counties = new ArrayList<County>();

            while (rs.next()) {
                County county = new County();

                county.setState(rs.getString("state"));
                county.setName(rs.getString("name"));
                
                counties.add(county);
            }

            return counties;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

    private final Connection db_conn;

}