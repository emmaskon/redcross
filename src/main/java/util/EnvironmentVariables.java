package util;

public class EnvironmentVariables {

    //Database info
    public static final String MYSQL_HOST = "localhost";//System.getenv("OPENSHIFT_MYSQL_DB_HOST");//
    public static final String MYSQL_PORT = "3306";//System.getenv("OPENSHIFT_MYSQL_DB_PORT");//
    public static final String MYSQL_SCHEMA = "system";
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_USER = "root";//"adminqUnjbVx";//
    public static final String MYSQL_PASSWORD = "";//EMqi5aH1D-c5";//"";
    public static final String MYSQL_URL = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/" + MYSQL_SCHEMA + "?useUnicode=yes&characterEncoding=UTF-8";
    public static final int RECAPTCHA_WEB_SERVICE_TIMEOUT = 5000; //ms
    public static final String RECAPTCHA_SITE_KEY = "6LcVeg4TAAAAAHjrkm7jfW79yiKlEuGpE_bVNd3z";
    public static final String RECAPTCHA_SECRET_KEY = "6LcVeg4TAAAAAHjNy-Dy96a4vJLNEoqsXfU2B2Ux";
    public static final String STORAGE_DIRECTORY = "C:\\vouchers";//System.getenv("OPENSHIFT_DATA_DIR");//"C:\\vouchers";//"/home/tomcat/storage/psychargos-costing";
    //public static final String PDF_FONT_FILEPATH = "/home/tomcat/fonts/courbd.ttf";
    public static final int UPLOAD_MAX_FILESIZE = 1024 * 2048; //bytes

    public class UsefulMYSQLErrorCodes {

        public static final int DUPLICATE_ENTRY = 1062;
    }
} 