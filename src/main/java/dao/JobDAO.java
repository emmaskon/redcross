package dao;

import java.util.ArrayList;
import util.exceptions.DataSourceException;
import models.Job;

public interface JobDAO {

    public ArrayList<Job> getAllJobs() throws DataSourceException;
    public String addJob(String job) throws DataSourceException;
    public String deleteJob(String job) throws DataSourceException;
}
