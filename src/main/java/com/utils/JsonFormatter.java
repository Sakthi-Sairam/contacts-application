package com.utils;
import java.util.*;

public class JsonFormatter extends HashMap<String,Object>{
    private static final long serialVersionUID = 1L;

	@Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean isFirst = true;
        for(Entry<String,Object> i: this.entrySet()){
            if(!isFirst) sb.append(",");
            sb.append("\""+i.getKey() +"\":");
            Object value = i.getValue();
            if (value instanceof Number || value instanceof Boolean){
                sb.append(value);
            }else{
                sb.append("\""+value+"\"");
            }
            isFirst = false;
        }
        sb.append("}");
        return sb.toString();
    }
}