package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.Municipality;

public interface MunicipalityDAO {

    public ArrayList<Municipality> getAllMunicipalities() throws DataSourceException;
    public String addMunicipality(String municipality_name) throws DataSourceException;
    public String deleteMunicipality(String municipality_name) throws DataSourceException;
}
