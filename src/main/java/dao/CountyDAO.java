package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.County;

public interface CountyDAO {

    public ArrayList<County> getAllCounties() throws DataSourceException;
    public ArrayList<County> getCounty(String name) throws DataSourceException;
    public ArrayList<County> getCountiesForState(String state_id) throws DataSourceException;
}
