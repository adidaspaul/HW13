import lombok.Data;

@Data
public class Task {

    private int userId, id;
    private String title;
    private boolean complete;
}
