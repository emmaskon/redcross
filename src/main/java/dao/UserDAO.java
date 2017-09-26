package dao;

import util.exceptions.DataSourceException;
import models.User;

public interface UserDAO {

    public User getUser(String username, String password) throws DataSourceException;
    
    public String getUsername(String username) throws DataSourceException;

    public boolean createUser(String username, String password) throws DataSourceException;

    public boolean updateUser(String username, String new_password) throws DataSourceException;
}
