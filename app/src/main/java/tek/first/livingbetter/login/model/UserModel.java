package tek.first.livingbetter.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by stan on 9/1/15.
 */
public class UserModel implements Parcelable{

    private int id;
    private String username;
    private String password;
    private String country;
    private int age;
    private String street;

    public UserModel(String username, String password, String country, int age, String street) {
        id = UUID.randomUUID().hashCode();
        this.username = username;
        this.password = password;
        this.country = country;
        this.age = age;
        this.street = street;
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        country = in.readString();
        age = in.readInt();
        street = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(country);
        dest.writeInt(age);
        dest.writeString(street);
    }
}
