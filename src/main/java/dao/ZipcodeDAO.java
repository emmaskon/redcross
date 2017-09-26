package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.Zipcode;

public interface ZipcodeDAO {

    public ArrayList<Zipcode> getAllZipcodes() throws DataSourceException;
    public String addZipcode(String zipcode) throws DataSourceException;
    public String deleteZipcode(String zipcode) throws DataSourceException;
}
