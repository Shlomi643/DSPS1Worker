package protocol;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ProtocolManager {

    private static Map<Integer, String> map = new HashMap<Integer, String>() {{
        put(10, MTerminate.class.getName());
        put(11, MTask.class.getName());
        put(21, MReady.class.getName());
        put(31, MJobSentiment.class.getName());
        put(32, MJobEntity.class.getName());
        put(41, MResponse.class.getName());
    }};

    public static MMessage parse(String json) {
        JSONObject obj = new JSONObject(json);
        int code = obj.getInt("code");
        int id = obj.getInt("id");
        String content = obj.getString("content");
        try {
            Constructor m = Class.forName(map.get(code)).getDeclaredConstructor(Integer.class, String.class);
            return (MMessage) m.newInstance(id, content);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int getCode(String name) {
        for (Map.Entry<Integer, String> entry : map.entrySet())
            if (name.equals(entry.getValue()))
                return entry.getKey();
        return 0;
    }

    public static String getString(String name, int id, String content) {
        JSONObject obj = new JSONObject();
        obj.put("code", ProtocolManager.getCode(name));
        obj.put("id", id);
        obj.put("content", content);
        return obj.toString();
    }
}

