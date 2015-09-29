package tek.first.livingbetter.helper;

import java.util.UUID;

/**
 * Created by stan on 9/1/15.
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String country;
    private int age;
    private String street;

    public User(String username, String password, String country, int age, String street) {
        id = UUID.randomUUID().hashCode();
        this.username = username;
        this.password = password;
        this.country = country;
        this.age = age;
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
