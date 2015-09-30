package tek.first.livingbetter.habit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoCollectedModel implements Parcelable{

    private String name;
    private float rating;
    private int numberComment;
    private String category;
    private String imageUrl;
    private String snippet_text;
    private String address;
    private String phoneNumber;
    private String mobileUrl;
    private double distance;
    private double latitude;
    private double longtitude;

    public InfoCollectedModel(String name, float rating, int numberComment, String category, String imageUrl, String snippet_text,
                              String address, String phoneNumber, String mobileUrl, double distance, double latitude, double longtitude) {
        this.name = name;
        this.rating = rating;
        this.numberComment = numberComment;
        this.category = category;
        this.imageUrl = imageUrl;
        this.snippet_text = snippet_text;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.mobileUrl = mobileUrl;
        this.distance = distance;
        this.latitude = latitude;
        this.longtitude=longtitude;
    }

    protected InfoCollectedModel(Parcel in) {
        name = in.readString();
        rating = in.readFloat();
        numberComment = in.readInt();
        category = in.readString();
        imageUrl = in.readString();
        snippet_text = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        mobileUrl = in.readString();
        distance = in.readDouble();
        latitude = in.readDouble();
        longtitude = in.readDouble();
    }

    public static final Creator<InfoCollectedModel> CREATOR = new Creator<InfoCollectedModel>() {
        @Override
        public InfoCollectedModel createFromParcel(Parcel in) {
            return new InfoCollectedModel(in);
        }

        @Override
        public InfoCollectedModel[] newArray(int size) {
            return new InfoCollectedModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberComment() {
        return numberComment;
    }

    public void setNumberComment(int numberComment) {
        this.numberComment = numberComment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSnippet_text() {
        return snippet_text;
    }

    public void setSnippet_text(String snippet_text) {
        this.snippet_text = snippet_text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(rating);
        dest.writeInt(numberComment);
        dest.writeString(category);
        dest.writeString(imageUrl);
        dest.writeString(snippet_text);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(mobileUrl);
        dest.writeDouble(distance);
        dest.writeDouble(latitude);
        dest.writeDouble(longtitude);
    }
}
