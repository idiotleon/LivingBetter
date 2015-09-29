package tek.first.livingbetter.todolist;

/**
 * Created by HAN KE on 2015/9/3.
 */
public class toDoItem {
    int id;
    int completestatus =0;
    String title="";
    String createtime="";
    int priority=0;
    String category="";

    public toDoItem(int completestatus, String title, String createtime, int priority, String category) {
        this.completestatus = completestatus;
        this.title = title;
        this.createtime = createtime;
        this.priority = priority;
        this.category = category;
    }

    public toDoItem(int id, int completestatus, String title, String createtime, int priority, String category) {
        this.id = id;
        this.completestatus = completestatus;
        this.title = title;
        this.createtime = createtime;
        this.priority = priority;
        this.category = category;
    }

    public int isCompletestatus() {
        return completestatus;
    }

    public void setCompletestatus(int completestatus) {
        this.completestatus = completestatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
