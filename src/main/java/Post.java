import lombok.Data;


@Data
public class Post {
    private int userId, id;
    private String title, body;


    @Override
    public String toString() {
        return "Post{" + "userId = " + userId +
                ", id = " + id + ", title = "
                + title + '\'' + ", body = "
                + body + '\''
                + '}';
    }
}
