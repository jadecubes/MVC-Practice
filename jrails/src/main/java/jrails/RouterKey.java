package jrails;
import java.lang.reflect.Field;

public class RouterKey {
    public String httpReq="";
    public String path="";
    public RouterKey(String verb,String p){
        httpReq=verb;
        path=p;
    }
    @Override
    public int hashCode() {
        return httpReq.hashCode()+path.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if(o==this) return true;
        if (!(o instanceof RouterKey)) return false;
        Field[] fields=getClass().getFields();
        for(int i=0;i<fields.length;++i){
            try {
                Field f =fields[i];
                Field of =o.getClass().getField(f.getName());
                if(!f.get(this).equals(of.get(o))) return false;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
