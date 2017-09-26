package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.State;

public interface StateDAO {

    public ArrayList<State> getAllStates() throws DataSourceException;
    public ArrayList<State> getState(String state_id) throws DataSourceException;
}
