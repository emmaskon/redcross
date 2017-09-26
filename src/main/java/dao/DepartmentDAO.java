package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.Department;

public interface DepartmentDAO {

    public ArrayList<Department> getAllDepartments() throws DataSourceException;
    public Department getDepartment(String name) throws DataSourceException;
    public Department getDepartment(String state_id, String city_id) throws DataSourceException;

    public ArrayList<Department> getAllPt() throws DataSourceException;
    public Department getPt(String name) throws DataSourceException;
    public ArrayList<Department> getPtAndTtsDepartments(String state_id) throws DataSourceException;
    public ArrayList<Department> getPtDepartment(String state_id, String city_id) throws DataSourceException;
    public ArrayList<Department> getTtDepartment(String state_id, String city_id) throws DataSourceException;
    
    public ArrayList<Department> getAllTt() throws DataSourceException;
    public Department getTt(String name) throws DataSourceException;
}
