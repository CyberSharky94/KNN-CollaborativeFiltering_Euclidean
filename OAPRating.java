// Outdoor Activity Places Ratings placed here.

public class OAPRating {
    private String username;
    private String placeName;
    private int rate;

    public OAPRating(String username, String placeName, int rate) {
        this.username = username;
        this.placeName = placeName;
        this.rate = rate;
    }

    public OAPRating(String username, String placeName) {
        this.username = username;
        this.placeName = placeName;
        this.rate = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "OAPRatings{" + "username=" + username + ", placeName=" + placeName + ", rate=" + rate + '}';
    }
}
