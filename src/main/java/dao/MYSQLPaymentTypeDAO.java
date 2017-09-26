package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.Payment_Type;
import util.exceptions.DataSourceException;

public class MYSQLPaymentTypeDAO implements PaymentTypeDAO {

    public MYSQLPaymentTypeDAO(Connection db_conn) {
        this.db_conn = db_conn;
    }

    @Override
    public ArrayList<Payment_Type> getAllPaymentTypes() throws DataSourceException {
        try {
            PreparedStatement pstmt = db_conn.prepareStatement(
                    "SELECT * FROM payment_types"
            );

            ResultSet rs = pstmt.executeQuery();

            ArrayList<Payment_Type> payment_types = new ArrayList<Payment_Type>();

            while (rs.next()) {
                Payment_Type payment_type = new Payment_Type();
                
                payment_type.setType(rs.getString("type"));
        
                payment_types.add(payment_type);
            }
            
            return payment_types;

        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

   
    private final Connection db_conn;

}
