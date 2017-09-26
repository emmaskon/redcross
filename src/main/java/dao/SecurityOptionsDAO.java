package dao;

import util.exceptions.DataSourceException;

public interface SecurityOptionsDAO {

    public boolean isRecaptchaEnabled() throws DataSourceException;
}
