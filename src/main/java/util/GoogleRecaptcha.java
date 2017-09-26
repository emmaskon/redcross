package util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

public class GoogleRecaptcha {

    public static boolean isRecaptchaCheckPassed(String user_recaptcha_response) throws MalformedURLException, IOException {
        URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(EnvironmentVariables.RECAPTCHA_WEB_SERVICE_TIMEOUT);
        conn.setConnectTimeout(EnvironmentVariables.RECAPTCHA_WEB_SERVICE_TIMEOUT);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
        writer.write("secret=" + EnvironmentVariables.RECAPTCHA_SECRET_KEY + "&response=" + user_recaptcha_response);
        writer.close();
        JsonReader json_reader = Json.createReader(conn.getInputStream());
        JsonObject root_obj = json_reader.readObject();
        boolean check_passed = root_obj.getBoolean("success");
        json_reader.close();
        return check_passed;
    }
}
