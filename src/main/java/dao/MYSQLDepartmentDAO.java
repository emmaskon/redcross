package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Department;
import util.exceptions.DataSourceException;

public class MYSQLDepartmentDAO implements DepartmentDAO {

    public MYSQLDepartmentDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Department> getAllDepartments() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments ORDER BY state_id ASC, city_id");

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Department> departments = new ArrayList<Department>();

            while (rs.next()) {
                Department department = new Department();

                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
                
                departments.add(department);
            }

            return departments;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Department getDepartment(String name) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE name=?");

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            Department department = new Department();

            while (rs.next()) {
                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
            }

            return department;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Department getDepartment(String state_id, String city_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE state_id=? AND city_id=?");

            pstmt.setString(1, state_id);
            pstmt.setString(2, city_id);
            ResultSet rs = pstmt.executeQuery();

            Department department = new Department();

            while (rs.next()) {
                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
            }

            return department;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Department> getAllPt() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE type=?");

            pstmt.setString(1, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Department> departments = new ArrayList<Department>();

            while (rs.next()) {
                Department department = new Department();

                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
                
                departments.add(department);
            }

            return departments;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Department getPt(String name) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE name=? AND type=?");

            pstmt.setString(1, name);
            pstmt.setString(2, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
            ResultSet rs = pstmt.executeQuery();

            Department department = new Department();

            while (rs.next()) {
                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
            }

            return department;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Department> getAllTt() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE type=?");

            pstmt.setString(1, "ΤΟΠΙΚΟ ΤΜΗΜΑ");
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Department> departments = new ArrayList<Department>();

            while (rs.next()) {
                Department department = new Department();

                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
                
                departments.add(department);
            }

            return departments;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public Department getTt(String name) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE name=? AND type=?");

            pstmt.setString(1, name);
            pstmt.setString(2, "ΤΟΠΙΚΟ ΤΜΗΜΑ");
            ResultSet rs = pstmt.executeQuery();

            Department department = new Department();

            while (rs.next()) {
                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
            }

            return department;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Department> getPtAndTtsDepartments(String state_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE state_id=?");
            
            pstmt.setString(1, state_id);
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Department> departments = new ArrayList<Department>();

            while (rs.next()) {
                Department department = new Department();

                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
                
                departments.add(department);
            }

            return departments;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Department> getPtDepartment(String state_id, String city_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE state_id=? AND city_id=? AND type=?");
            
            pstmt.setString(1, state_id);
            pstmt.setString(2, city_id);
            pstmt.setString(3, "ΠΕΡΙΦΕΡΕΙΑΚΟ ΤΜΗΜΑ");
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Department> departments = new ArrayList<Department>();

            while (rs.next()) {
                Department department = new Department();

                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
                
                departments.add(department);
            }

            return departments;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
    
    @Override
    public ArrayList<Department> getTtDepartment(String state_id, String city_id) throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement("SELECT * FROM departments WHERE state_id=? AND city_id=? AND type=?");
            
            pstmt.setString(1, state_id);
            pstmt.setString(2, city_id);
            pstmt.setString(3, "ΤΟΠΙΚΟ ΤΜΗΜΑ");
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Department> departments = new ArrayList<Department>();

            while (rs.next()) {
                Department department = new Department();

                department.setName(rs.getString("name"));
                department.setType(rs.getString("type"));
                department.setStateId(rs.getString("state_id"));
                department.setCityId(rs.getString("city_id"));
                department.setCounty(rs.getString("county"));
                
                departments.add(department);
            }

            return departments;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

    private final Connection db_conn;

}