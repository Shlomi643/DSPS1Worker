package actors.control;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HTMLBuilder {

    /*

    Text is:
    "[{
        "id": String
        "review": String
        "rating": int
        "sentiment": String
        "entity": String
     }, ...]"

    */

    private static Map<String, String> codes = new HashMap<String, String>() {{
        put("0", "#B03A2E");
        put("1", "#EC7063");
        put("2", "#000000");
        put("3", "#58D68D");
        put("4", "#229954");
    }};

    public static void build(String text, String out) {
        String location = System.getProperty("user.dir") + "\\outputs\\"
                + out + (out.endsWith(".html") ? "" : ".html");
        File file = new File(location);
        try {
            if (file.createNewFile())
                System.out.println("File Created");
            StringBuilder builder = startHTML(text);
            FileWriter writer = new FileWriter(file);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder startHTML(String text) {
        StringBuilder ret = new StringBuilder();
        ret.append("<!DOCTYPE html>").append("\n")
                .append("<html>").append("\n")
                .append("<head>").append("\n")
                .append("<style>").append("\n")
                .append("table {").append("\n")
                .append("\t").append("border-collapse: collapse;").append("\n")
                .append("}").append("\n")
                .append("table, th {").append("\n")
                .append("\t").append("border: 1px solid black;").append("\n")
                .append("}").append("\n")
                .append("td {").append("\n")
                .append("\t").append("border: 1px solid gray;").append("\n")
                .append("}").append("\n")
                .append("</style>").append("\n")
                .append("</head>").append("\n")
                .append("<body>").append("\n")
                .append("<table>").append("\n")
                .append("<tr>").append("\n")
                .append("<th>Review ID</th>").append("\n")
                .append("<th>Review</th>").append("\n")
                .append("<th>Entities</th>").append("\n")
                .append("<th>Sarcasm</th>").append("\n")
                .append("</tr>").append("\n");

        JSONArray arr = new JSONArray(text);
        for (Object obj : arr) {
            JSONObject review = new JSONObject(obj.toString());
            String sentiment = review.getString("sentiment");
            ret.append("<tr style=\"color: ").append(codes.get(sentiment)).append("\">").append("\n")
                    .append("<td>")
                    .append(review.getString("id"))
                    .append("</td>").append("\n")
                    .append("<td>")
                    .append(review.getString("review"))
                    .append("</td>").append("\n")
                    .append("<td>")
                    .append(review.getString("entity"))
                    .append("</td>").append("\n")
                    .append("<td>")
                    .append(isSarcasm(sentiment, review.getInt("rating")) ? "Sarcasm found" : "No sarcasm")
                    .append("</td>").append("\n")
                    .append("</tr>").append("\n");
        }

        ret.append("</table>").append("\n")
                .append("</body>").append("\n")
                .append("</html>").append("\n");

        return ret;
    }

    // TODO check
    private static boolean isSarcasm(String sentiment, int rating) {
        int sent = Integer.parseInt(sentiment);
        return Math.abs(sent - rating - 1) >=2;
    }

//    public static void main(String[] args) {
//        String fuck = "hello/goodbye/file";
//        System.out.println(fuck.substring(fuck.lastIndexOf("/") + 1));
//    }

    // Test Test Test
    public static void main(String[] args) {
        JSONArray arr = new JSONArray();
        JSONObject ob1 = new JSONObject();
        ob1.put("id", "1");
        ob1.put("rating", 5);
        ob1.put("review", "fuck you");
        ob1.put("sentiment", "0");
        ob1.put("entity", "[fuck, you, lol, shit]");
        JSONObject ob2 = new JSONObject();
        ob2.put("id", "1");
        ob2.put("review", "fuck you");
        ob2.put("sentiment", "1");
        ob2.put("entity", "[fuck, you, lol, shit]");
        JSONObject ob3 = new JSONObject();
        ob3.put("review", "fuck you");
        ob3.put("sentiment", "2");
        ob3.put("entity", "[fuck, you, lol, shit]");
        JSONObject ob4 = new JSONObject();
        ob4.put("review", "fuck you");
        ob4.put("sentiment", "4");
        ob4.put("entity", "[fuck, you, lol, shit]");
        JSONObject ob5 = new JSONObject();
        ob5.put("review", "fuck you");
        ob5.put("sentiment", "3");
        ob5.put("entity", "[fuck, you, lol, shit]");
        JSONObject ob6 = new JSONObject();
        ob6.put("review", "fuck you");
        ob6.put("sentiment", "1");
        ob6.put("entity", "[fuck, you, lol, shit]");
        ob3.put("id", "1");
        ob4.put("id", "1");
        ob5.put("id", "1");
        ob6.put("id", "1");
        ob2.put("rating", 5);
        ob3.put("rating", 5);
        ob4.put("rating", 5);
        ob5.put("rating", 5);
        ob6.put("rating", 5);
        arr.put(ob1);
        arr.put(ob2);
        arr.put(ob3);
        arr.put(ob4);
        arr.put(ob5);
        arr.put(ob6);
        System.out.println(startHTML(arr.toString()));
        build(arr.toString(), "fuckyouandyouandyouuuuuuu");
    }
//    */
}
