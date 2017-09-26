package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Subscription_Type;
import util.exceptions.DataSourceException;

public class MYSQLSubscriptionTypeDAO implements SubscriptionTypeDAO {

    public MYSQLSubscriptionTypeDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Subscription_Type> getAllSubscriptionTypes() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT * FROM subscription_types"
            );

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Subscription_Type> subscription_types = new ArrayList<Subscription_Type>();

            while (rs.next()) {
                Subscription_Type subscription_type = new Subscription_Type();
                
                subscription_type.setType(rs.getString("type"));
                subscription_type.setCost(rs.getString("cost"));
        
                subscription_types.add(subscription_type);
            }
            
            return subscription_types;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

   
    private final Connection db_conn;

}
