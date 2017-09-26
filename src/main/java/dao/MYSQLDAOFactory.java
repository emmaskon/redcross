package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import util.EnvironmentVariables;
import util.exceptions.DataSourceException;

public class MYSQLDAOFactory extends DAOFactory {

    public MYSQLDAOFactory() throws SQLException, ClassNotFoundException {
        // Register JDBC driver
        Class.forName(EnvironmentVariables.JDBC_DRIVER);

        // Open a connection
        db_conn = DriverManager.getConnection(
                EnvironmentVariables.MYSQL_URL,
                EnvironmentVariables.MYSQL_USER,
                EnvironmentVariables.MYSQL_PASSWORD
        );
    }

    @Override
    public UserDAO getUserDAO() throws DataSourceException {
        if (user_dao == null) {
            user_dao = new MYSQLUserDAO(db_conn);
        }

        return user_dao;
    }
    
    @Override
    public MemberDAO getMemberDAO() throws DataSourceException {
        if (member_dao == null) {
            member_dao = new MYSQLMemberDAO(db_conn);
        }

        return member_dao;
    }
    
    @Override
    public MemberEconomicDAO getMemberEconomicDAO() throws DataSourceException {
        if (member_economic_dao == null) {
            member_economic_dao = new MYSQLMemberEconomicDAO(db_conn);
        }

        return member_economic_dao;
    }
    
    @Override
    public DepartmentDAO getDepartmentDAO() throws DataSourceException {
        if (department_dao == null) {
            department_dao = new MYSQLDepartmentDAO(db_conn);
        }

        return department_dao;
    }
    
    @Override
    public StateDAO getStateDAO() throws DataSourceException {
        if (state_dao == null) {
            state_dao = new MYSQLStateDAO(db_conn);
        }

        return state_dao;
    }
    
    @Override
    public CountyDAO getCountyDAO() throws DataSourceException {
        if (county_dao == null) {
            county_dao = new MYSQLCountyDAO(db_conn);
        }

        return county_dao;
    }
    
    @Override
    public DoyDAO getDoyDAO() throws DataSourceException {
        if (doy_dao == null) {
            doy_dao = new MYSQLDoyDAO(db_conn);
        }

        return doy_dao;
    }
    
    @Override
    public MunicipalityDAO getMunicipalityDAO() throws DataSourceException {
        if (municipality_dao == null) {
            municipality_dao = new MYSQLMunicipalityDAO(db_conn);
        }

        return municipality_dao;
    }
    
    @Override
    public JobDAO getJobDAO() throws DataSourceException {
        if (job_dao == null) {
            job_dao = new MYSQLJobDAO(db_conn);
        }

        return job_dao;
    }
    
    @Override
    public ZipcodeDAO getZipcodeDAO() throws DataSourceException {
        if (zipcode_dao == null) {
            zipcode_dao = new MYSQLZipcodeDAO(db_conn);
        }

        return zipcode_dao;
    }
    
    @Override
    public SecurityOptionsDAO getSecurityOptionsDAO() throws DataSourceException {
        if (security_options_dao == null) {
            security_options_dao = new MYSQLSecurityOptionsDAO(db_conn);
        }

        return security_options_dao;
    }
    
    @Override
    public PaymentTypeDAO getPaymentTypeDAO() throws DataSourceException {
        if (payment_type_dao == null) {
            payment_type_dao = new MYSQLPaymentTypeDAO(db_conn);
        }

        return payment_type_dao;
    }
    
    @Override
    public SubscriptionTypeDAO getSubscriptionTypeDAO() throws DataSourceException {
        if (subscription_type_dao == null) {
            subscription_type_dao = new MYSQLSubscriptionTypeDAO(db_conn);
        }

        return subscription_type_dao;
    }
    
    @Override
    public LogDAO getLogDAO() throws DataSourceException {
        if (log_dao == null) {
            log_dao = new MYSQLLogDAO(db_conn);
        }

        return log_dao;
    }
    
    @Override
    public AnnualSchedulerDAO getAnnualSchedulerDAO() throws DataSourceException {
        if (annual_scheduler_dao == null) {
            annual_scheduler_dao = new MYSQLAnnualSchedulerDAO(db_conn);
        }

        return annual_scheduler_dao;
    }

    @Override
    public void startTransaction() throws DataSourceException {
        try {
            db_conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            db_conn.setAutoCommit(false);
            is_transaction_mode_active = true;
        } catch (SQLException ex) {
            throw new DataSourceException(ex);
        }
    }

    @Override
    public void abortTransaction() throws DataSourceException {
        try {
            db_conn.rollback();
        } catch (SQLException ex) {
            throw new DataSourceException(ex);
        }
    }

    @Override
    public void endTransaction() throws DataSourceException {
        try {
            db_conn.commit();
            is_transaction_mode_active = false;
        } catch (SQLException ex) {
            throw new DataSourceException(ex);
        }
    }

    @Override
    public void release() throws DataSourceException {
        try {
            if (is_transaction_mode_active) {
                abortTransaction();
            }
            db_conn.close();
        } catch (SQLException ex) {
            throw new DataSourceException(ex.getMessage());
        }
    }

    private Connection db_conn = null;
    private UserDAO user_dao = null;
    private MemberDAO member_dao = null;
    private MemberEconomicDAO member_economic_dao = null;
    private DepartmentDAO department_dao = null;
    private StateDAO state_dao = null;
    private CountyDAO county_dao = null;
    private DoyDAO doy_dao = null;
    private MunicipalityDAO municipality_dao = null;
    private ZipcodeDAO zipcode_dao = null;
    private JobDAO job_dao = null;
    private SecurityOptionsDAO security_options_dao = null;
    private PaymentTypeDAO payment_type_dao = null;
    private SubscriptionTypeDAO subscription_type_dao = null;
    private LogDAO log_dao = null;
    private AnnualSchedulerDAO annual_scheduler_dao = null;
    private boolean is_transaction_mode_active = false;
}
