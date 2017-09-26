package dao;

import models.Annual_Scheduler;
import util.exceptions.DataSourceException;

public interface AnnualSchedulerDAO {

    public Annual_Scheduler getAnnualScheduler() throws DataSourceException;
    public String updateAnnualScheduler(int current_year, int pre_year) throws DataSourceException;

}