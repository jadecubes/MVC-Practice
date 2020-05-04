package jrails;

import java.lang.reflect.Method;
import java.util.*;

public class JRouter {
    HashMap<RouterKey,RouterVal> handler=new HashMap<>();
    public void addRoute(String verb, String path, Class clazz, String method) {
        //verb=verb.replaceAll(" ","");
        //method=method.replaceAll(" ","");
        handler.put(new RouterKey(verb,path),new RouterVal(clazz,method));
    }

    // Returns "clazz#method" corresponding to verb+URN
    // Null if no such route
    public String getRoute(String verb, String path) {
        RouterVal v=handler.get(new RouterKey(verb,path));
        if(v==null)
            return null;
        else
            return v.clazz.getSimpleName()+"#"+v.method;
    }

    // Call the appropriate controller method and
    // return the result
    public Html route(String verb, String path, Map<String, String> params) {
        RouterVal v=handler.get(new RouterKey(verb,path));
        if(v==null)
            throw new UnsupportedOperationException();
        else{
            try {
                Method m=v.clazz.getMethod(v.method,Map.class);
                return (Html)m.invoke(null,params);
            } catch (Exception e) {
                throw new UnsupportedOperationException();
            }
        }

    }
}