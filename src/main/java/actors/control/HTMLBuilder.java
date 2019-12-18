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
        System.out.println("in build with" + text + " to " + out);
        String location = System.getProperty("user.dir") + "\\outputs\\"
                + out + (out.endsWith(".html") ? "" : ".html");
        File file = new File(location);
        System.out.println("to file " + file.getName());
        try {
            if (file.createNewFile())
                System.out.println("File Created");
            System.out.println("startingbuild");
            StringBuilder builder = startHTML(text);
            FileWriter writer = new FileWriter(file);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder startHTML(String text) {
        System.out.println("in startHTML");
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
        return Math.abs(sent + 1 - rating) >= 2;
    }

//    public static void main(String[] args) {
//        String fuck = "hello/goodbye/file";
//        System.out.println(fuck.substring(fuck.lastIndexOf("/") + 1));
//    }

    // Test Test Test
    public static void main(String[] args) {
        JSONArray arr = new JSONArray("[{\"sentiment\":\"3\",\"review\":\"As soon as I got this package, I gave it to my 18 month old son to open and he made me read it to him about a dozen times. He loves it! He's obsessed with his belly button, so this is the perfect book for him. He's also obsessed with peek-a-boo, so he loves lifting the flaps. It was a definite hit!\",\"rating\":5,\"id\":\"RJ62XWHGDIF7\",\"entity\":\"[NUMBER, TIME, TITLE, DURATION]\"},{\"sentiment\":\"3\",\"review\":\"My girls love this book so much I bought it for my niece!\",\"rating\":5,\"id\":\"RSO743RDYVWTZ\",\"entity\":\"[TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"Love Karen Katz\",\"rating\":5,\"id\":\"R8OWCLDI0GGL0\",\"entity\":\"[NUMBER, TIME, TITLE]\"},{\"sentiment\":\"3\",\"review\":\"Does a nice job of showing your child where different body parts are in a cute manner.  However, my one year old likes to lift the flaps himself and I also think this is a great way for him to be part of the story too.  The problem with this book is the flaps covering the different body parts are very flimsy and very easily ruined.  Compared to some other books out there, this one doesn't live up to expectations.  Youngsters are still figuring out motor skills and are anything but delicate at this stage.  I appreciate books that are made to last more than a couple reads...\",\"rating\":3,\"id\":\"R33DK4T6EHUIHX\",\"entity\":\"[NUMBER, TIME, TITLE, DURATION]\"},{\"sentiment\":\"3\",\"review\":\"Very cute!\",\"rating\":5,\"id\":\"R3DHV2T9Q991V7\",\"entity\":\"[NUMBER, TIME, TITLE]\"},{\"sentiment\":\"2\",\"review\":\"Super cute book. My son loves lifting the flaps.\",\"rating\":5,\"id\":\"R14D3WP6J91DCU\",\"entity\":\"[NUMBER, TIME, TITLE]\"},{\"sentiment\":\"3\",\"review\":\"So cute and fun.\",\"rating\":5,\"id\":\"R3P2KOXRF7Z400\",\"entity\":\"[PERSON, TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"My daughter loves lifting the flaps herself! She's almost a year. Great colorful pictures. I'm never disappointed with Karen Katz!\",\"rating\":5,\"id\":\"R1IKZK5S0DCKZ0\",\"entity\":\"[PERSON, TIME, TITLE, DURATION]\"},{\"sentiment\":\"1\",\"review\":\"Well-made children's book that an child will love.\",\"rating\":5,\"id\":\"RGMH5ROASTBS8\",\"entity\":\"[NUMBER, TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"Cute lift-the-flap book. My 9 month old loves it!\",\"rating\":5,\"id\":\"R1RUXYHCSZSHJ0\",\"entity\":\"[TIME, TITLE, DURATION]\"},{\"sentiment\":\"2\",\"review\":\"This gift was liked by the child and parents.\",\"rating\":5,\"id\":\"R3R9QKZ61DMKGF\",\"entity\":\"[PERSON, TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"I can't say much about this except that it was a gift for a great granddaughter.  My granddaughter tells me that she reads this to her eighteen month old daughter, and they both enjoy it enough to share it over and over again.  I read it too and found it a fun story.\",\"rating\":5,\"id\":\"R367OTNJ8I36FS\",\"entity\":\"[TIME, TITLE, DURATION]\"},{\"sentiment\":\"1\",\"review\":\"This book is adorable. My toddler loves it.\",\"rating\":5,\"id\":\"R3GCFAIUL8BI3Y\",\"entity\":\"[NUMBER, PERSON, TIME, TITLE]\"},{\"sentiment\":\"3\",\"review\":\"Such an entertaining book, my daughter loves books so this is a great look and find book... lol\",\"rating\":5,\"id\":\"R33SW6AMDKTSB8\",\"entity\":\"[PERSON, TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"I bought this book for a newborn. She loved when our parents read to her as an infant. She's now almost two and loves playing with it herself. I got it based on the positive reviews. I'm so glad I did. Will buy it again and highly recommend\",\"rating\":5,\"id\":\"RW6ORU2M3C461\",\"entity\":\"[DATE, NUMBER, TIME, TITLE]\"},{\"sentiment\":\"3\",\"review\":\"The book is sturdy and well made. My baby likes to carry her books around and they are routinely dropped. It has held up really well. She loves the flaps and finding the hands, feet, etc. My only critique is that the body parts that are highlighted are the smallest item on the whole page. As a visual representation for teaching a child it seems weird that the focal points are not scaled to be more familiarly identifiable.\",\"rating\":4,\"id\":\"R1L8O7RM8DD6J3\",\"entity\":\"[TIME, TITLE, DURATION]\"},{\"sentiment\":\"1\",\"review\":\"Cutest book ever. All my kids love, love, love it!! I'd give this as a present to other people too.\",\"rating\":5,\"id\":\"R3V239ZPPF0XY3\",\"entity\":\"[DATE, PERSON, TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"My almost 2 year old son loves the Katz books. We have the Beach Ball book, the Zoom Zoom book, and this one, so far, but I'm sure we'll get more. My son really likes this one, though I think he prefers the Beach Ball bookֲ Where Is Baby's Beach Ball?: A Lift-the-Flap Book (Karen Katz Lift-the-Flap Books)ֲ best, but all the Katz books are top faves.\",\"rating\":5,\"id\":\"R3MRIY8FUDOYHU\",\"entity\":\"[NUMBER, PERSON, TIME, TITLE, DURATION]\"},{\"sentiment\":\"1\",\"review\":\"love\",\"rating\":5,\"id\":\"RB9QK3F6SUQPU\",\"entity\":\"[TIME, TITLE]\"},{\"sentiment\":\"1\",\"review\":\"Cute!\",\"rating\":5,\"id\":\"R366N97DLUODS7\",\"entity\":\"[TIME, TITLE]\"}]");
        System.out.println(startHTML(arr.toString()));
        build(arr.toString(), "fuckyouandyouandyouuuuuuu");
    }
//    */
}
