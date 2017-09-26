package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Zipcode;
import util.exceptions.DataSourceException;

public class MYSQLZipcodeDAO implements ZipcodeDAO {

    public MYSQLZipcodeDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Zipcode> getAllZipcodes() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM zipcodes");
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Zipcode> zipcodes = new ArrayList<Zipcode>();

            while (rs.next()) {
                Zipcode zipcode = new Zipcode();

                zipcode.setZipcode(rs.getString("zipcode"));
                
                PreparedStatement pstmt2 = db_conn.prepareStatement("SELECT * FROM zipcode_ranges WHERE from_num<=? AND to_num>=?");
                
                int current_zipcode=0;
                try{
                    current_zipcode = Integer.parseInt(zipcode.getZipcode());
                }catch(Exception e){}
                
                pstmt2.setInt(1, current_zipcode);
                pstmt2.setInt(2, current_zipcode);
                ResultSet rs2 = pstmt2.executeQuery();
                
                while (rs2.next()) {
                    zipcode.setFrom_num(rs2.getString("from_num"));
                    zipcode.setTo_num(rs2.getString("to_num"));
                    zipcode.setArea(rs2.getString("area"));
                }
                
                zipcodes.add(zipcode);
            }

            return zipcodes;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public String addZipcode(String zipcode) throws DataSourceException {
        String result="unknown";
        
        try {

            PreparedStatement pstmt = db_conn.prepareStatement(
                    "INSERT INTO zipcodes(zipcode) VALUES (?)"
            );
            
            pstmt.setString(1,  zipcode);

            pstmt.executeUpdate();

            result = "insert ok";
        } catch (Exception ex) {
            result="not ok";
        }
        
        return result;
    }
    
    @Override
    public String deleteZipcode(String zipcode) throws DataSourceException{
        
        String result = "unknown";
        
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "DELETE FROM zipcodes WHERE zipcode=?"
            );

            pstmt.setInt(1, Integer.parseInt(zipcode));
            pstmt.executeUpdate();

            result = "delete ok";
        } catch (Exception ex) {
            result = "not ok";
        }
        
        return result;
    }

    private final Connection db_conn;

}