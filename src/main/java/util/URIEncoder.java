package util;

import java.net.URISyntaxException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class URIEncoder {

    public static String encodeURI(String uri) {
        try {
            return js_engine.eval("encodeURI(\"" + uri + "\")").toString();
        } catch (ScriptException ex) {
            return null; //never happens
        }
    }

    public static String encodeURIComponent(String uri_component) {
        try {
            return js_engine.eval("encodeURIComponent(\"" + uri_component + "\")").toString();
        } catch (ScriptException ex) {
            return null; //never happens
        }
    }

    private static final ScriptEngine js_engine = new ScriptEngineManager().getEngineByName("JavaScript");
}
