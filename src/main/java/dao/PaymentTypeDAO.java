package dao;

import java.util.ArrayList;
import models.Payment_Type;
import util.exceptions.DataSourceException;

public interface PaymentTypeDAO {

    public ArrayList<Payment_Type> getAllPaymentTypes() throws DataSourceException;

}