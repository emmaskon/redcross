package util;

import java.net.URISyntaxException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class URIDecoder {

    public static String decodeURI(String uri) {
        try {
            return js_engine.eval("decodeURI(\"" + uri + "\")").toString();
        } catch (ScriptException ex) {
            return null; //never happens
        }
    }

    public static String decodeURIComponent(String uri_component) {
        try {
            return js_engine.eval("decodeURIComponent(\"" + uri_component + "\")").toString();
        } catch (ScriptException ex) {
            return null; //never happens
        }
    }

    private static final ScriptEngine js_engine = new ScriptEngineManager().getEngineByName("JavaScript");
}
