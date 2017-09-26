package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.Doy;

public interface DoyDAO {

    public ArrayList<Doy> getAllDoys() throws DataSourceException;
    public String addDoy(String doy) throws DataSourceException;
    public String deleteDoy(String doy) throws DataSourceException;
}
