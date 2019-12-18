package protocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ProtocolManager {

    private static Map<Integer, String> map = new HashMap<Integer, String>() {{
        put(10, MTaskTerminate.class.getName());
        put(11, MTaskDownload.class.getName());
        put(21, MReady.class.getName());
        put(31, MJobSentiment.class.getName());
        put(32, MJobEntity.class.getName());
        put(41, MResponseSentiment.class.getName());
        put(42, MResponseEntity.class.getName());
    }};

    private static MMessage parseHelper(String json) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JSONObject obj = new JSONObject(json);
        int code = obj.getInt("code");
        String id = obj.getString("id");
        String content = obj.getString("content");

        try {
            String filename = obj.getString("filename");
            String reviewID = obj.getString("reviewID");
            Constructor m = Class.forName(map.get(code)).getDeclaredConstructor(String.class, String.class, String.class, String.class);
            return (MMessage) m.newInstance(id, filename, reviewID, content);
        } catch (JSONException e) {
            Constructor m = Class.forName(map.get(code)).getDeclaredConstructor(String.class, String.class);
            return (MMessage) m.newInstance(id, content);
        }
    }

    public static MMessage parse(String json) {
        try {
            return parseHelper(json);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getCode(String name) {
        for (Map.Entry<Integer, String> entry : map.entrySet())
            if (name.equals(entry.getValue()))
                return entry.getKey();
        return 0;
    }

    public static String getString(String name, String id, String content) {
        JSONObject obj = new JSONObject();
        obj.put("code", ProtocolManager.getCode(name));
        obj.put("id", id);
        obj.put("content", content);
        return obj.toString();
    }

    public static String getString(String name, String id, String filename, String reviewID, String content) {
        JSONObject obj = new JSONObject();
        obj.put("code", ProtocolManager.getCode(name));
        obj.put("id", id);
        obj.put("filename", filename);
        obj.put("reviewID", reviewID);
        obj.put("content", content);
        return obj.toString();
    }
}

