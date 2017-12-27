package samplepush2.androidtown.org.iotapplication.model;

/**
 * Created by MinWoo on 2017-12-20.
 */

public class Person {

    private String id;
    private String pass;
    private String token;

    public String getID() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
