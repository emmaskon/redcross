package dao;

import util.exceptions.DataSourceException;

public abstract class DAOFactory {

    public abstract UserDAO getUserDAO() throws DataSourceException;
    
    public abstract MemberDAO getMemberDAO() throws DataSourceException;
    
    public abstract MemberEconomicDAO getMemberEconomicDAO() throws DataSourceException;
    
    public abstract DepartmentDAO getDepartmentDAO() throws DataSourceException;
    
    public abstract StateDAO getStateDAO() throws DataSourceException;
    
    public abstract CountyDAO getCountyDAO() throws DataSourceException;
    
    public abstract DoyDAO getDoyDAO() throws DataSourceException;
    
    public abstract MunicipalityDAO getMunicipalityDAO() throws DataSourceException;

    public abstract ZipcodeDAO getZipcodeDAO() throws DataSourceException;
    
    public abstract JobDAO getJobDAO() throws DataSourceException;
    
    public abstract SecurityOptionsDAO getSecurityOptionsDAO() throws DataSourceException;
    
    public abstract PaymentTypeDAO getPaymentTypeDAO() throws DataSourceException;
    
    public abstract SubscriptionTypeDAO getSubscriptionTypeDAO() throws DataSourceException;
    
    public abstract LogDAO getLogDAO() throws DataSourceException;
    
    public abstract AnnualSchedulerDAO getAnnualSchedulerDAO() throws DataSourceException;
    
    public static DAOFactory getDAOFactory(int factory) throws DataSourceException {

        switch (factory) {
            case MYSQL:
                try {
                    return new MYSQLDAOFactory();
                } catch (Exception ex) {
                    throw new DataSourceException(ex);
                }
            default:
                throw new DataSourceException("Τhe selected dao factory is not supported");
        }
    }

    public abstract void startTransaction() throws DataSourceException;

    public abstract void abortTransaction() throws DataSourceException;

    public abstract void endTransaction() throws DataSourceException;

    public abstract void release() throws DataSourceException;

    public static final int MYSQL = 0;
}
