package controllers.log;

import dao.DAOFactory;
import java.util.Calendar;
import java.util.Date;

public class InsertLogRecord {

   public static boolean insert(String username, String message){
       
        boolean result = false;
       
        try {
            DAOFactory dao_factory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);

            Date date=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String dayStr = day+"";
            int month = cal.get(Calendar.MONTH)+1;
            String monthStr = month+"";
            int year = cal.get(Calendar.YEAR);

            if(day<10){
                dayStr = "0"+dayStr;
            }

            if(month<10){
                monthStr = "0"+monthStr;
            }

            int minute = cal.get(Calendar.MINUTE);
            String minuteStr = minute+"";
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            String hourStr = hour+"";

            if(minute<10){
                minuteStr = "0"+minuteStr;
            }

            if(hour<10){
                hourStr = "0"+hourStr;
            }

            String current_date = dayStr+"/"+monthStr+"/"+year;
            String current_time = hourStr+":"+minuteStr;

            result = dao_factory.getLogDAO().insertLog(username, current_date, current_time, message);

            if (dao_factory != null) {
                try {
                    dao_factory.release();
                } catch (Exception ex) {
                    //Do nothing
                }
            }
        }catch (Exception e) {}
        
        return result;
   }
}
