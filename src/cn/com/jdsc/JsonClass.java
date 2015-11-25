package cn.com.jdsc;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zoson on 3/15/15.
 */
public class JsonClass{
    private JSONObject jo;
    private JSONArray ja;
    public JsonClass(String jsonString) {
        try{
            jo = new JSONObject(jsonString);
            ja = new JSONArray();
        }catch (Exception e){
            System.out.println("Json´íÎó");
            e.printStackTrace();
        }

    }
    public String getString(String key){
        try{
            return jo.getString(key);
        }catch (Exception e){
            System.out.println("Json½âÎö");
            e.printStackTrace();
        }
        return "-1";
    }
    public Boolean getBoolean(String key){
        try{
            return jo.getBoolean(key);
        }catch (Exception e){
            System.out.println("Json½âÎö");
            e.printStackTrace();
        }
        return false;
    }
    public int getInt(String key){
        try {
            return jo.getInt(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}
