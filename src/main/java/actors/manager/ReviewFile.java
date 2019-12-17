package actors.manager;

import java.util.ArrayList;
import java.util.List;

public class ReviewFile {

    private List<String> reviews;
    private int numOfReviews;

    public ReviewFile(String reviews) {
        this.reviews = new ArrayList<>();

        addReviews(reviews);

        this.numOfReviews = reviews.length();
    }

    private void addReviews(String reviews) {
        StringBuilder s = new StringBuilder();
        for (String line : reviews.split("\n")) {
            s.append(line);
            if (line.equals("}")) {
                this.reviews.add(s.toString());
                s = new StringBuilder();
            }

        }
    }

    public List<String> getReviews() {
        return reviews;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }
}
