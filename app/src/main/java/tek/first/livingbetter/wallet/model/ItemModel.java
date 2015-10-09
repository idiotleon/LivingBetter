package tek.first.livingbetter.wallet.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/9/1.
 */
public class ItemModel implements Parcelable{
    private int id;
    private String date;
    private String cate;
    private String expense;
    private String title;

    public ItemModel(int id, String title, String date, String cate, String expense) {
        this.id = id;
        this.date = date;
        this.cate = cate;
        this.expense = expense;
        this.title = title;
    }

    public ItemModel(int id, String title, String date, String cate) {
        this.id = id;
        this.date = date;
        this.cate = cate;
        this.title = title;
    }


    protected ItemModel(Parcel in) {
        id = in.readInt();
        date = in.readString();
        cate = in.readString();
        expense = in.readString();
        title = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(cate);
        dest.writeString(expense);
        dest.writeString(title);
    }
}
