package samplepush2.androidtown.org.iotapplication.model;

/**
 * Created by MinWoo on 2017-11-23.
 */

public class Trash {
    public int image;
    public String location;
    public String amount;

    public Trash(int image, String location, String amount) {
        this.image = image;
        this.location = location;
        this.amount = amount;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getAmount() {
        return amount;
    }
}
