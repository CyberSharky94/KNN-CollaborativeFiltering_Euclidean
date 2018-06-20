public class OutdoorActivityPlace {
    private int id;
    private String placeName;
    private String address;

    public OutdoorActivityPlace(int id, String placeName, String address) {
        this.id = id;
        this.placeName = placeName;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "OutdoorActivityPlaces{" + "id=" + id + ", placeName=" + placeName + ", address=" + address + '}';
    }

    
}
