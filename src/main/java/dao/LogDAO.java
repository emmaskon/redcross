package dao;

import java.util.ArrayList;
import models.Log;
import util.exceptions.DataSourceException;

public interface LogDAO {

    public ArrayList<Log> getLog(String fromdate, String todate) throws DataSourceException;

    public boolean insertLog(String username, String date, String time, String action) throws DataSourceException;
}
