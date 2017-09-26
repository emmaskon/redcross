package dao;

import java.util.ArrayList;
import models.Subscription_Type;
import util.exceptions.DataSourceException;

public interface SubscriptionTypeDAO {

    public ArrayList<Subscription_Type> getAllSubscriptionTypes() throws DataSourceException;

}