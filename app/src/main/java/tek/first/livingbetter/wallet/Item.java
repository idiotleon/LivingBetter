package tek.first.livingbetter.wallet;

/**
 * Created by Administrator on 2015/9/1.
 */
public class Item {
    private int id;
    private String date;
    private String cate;
    private String expense;
    private String title;

    public Item(int id, String title,String date, String cate, String expense) {
        this.id = id;
        this.date = date;
        this.cate = cate;
        this.expense = expense;
        this.title = title;
    }

    public Item(int id, String title,String date, String cate) {
        this.id = id;
        this.date = date;
        this.cate = cate;
        this.title = title;
    }



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
}
