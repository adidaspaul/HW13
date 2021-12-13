import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    public static final Gson GSON = new Gson();
    public static final String DEFAULT_URI = "https://jsonplaceholder.typicode.com";
    public static final String USERS = "/users";
    public static final String POSTS = "/posts";
    public static final String TODO = "/todos";
    public static final String COMMENTS = "/comments";

    public static String createUser(User user) throws IOException, InterruptedException {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DEFAULT_URI + USERS))
                    .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(user)))
                    .header("Content-Type", "application/json")
                    .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String updateUser(int userId, User updatedUser) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(updatedUser);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d",DEFAULT_URI, USERS, userId)))
                .header("Content_Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static int deleteUser(User user) throws IOException, InterruptedException{
        String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", DEFAULT_URI, USERS, user.getId())))
                .header("Content_Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> getAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DEFAULT_URI + USERS))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), new TypeToken<List<User>>() {}.getType());
    }

    public static User getUserId(int id) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", DEFAULT_URI, USERS, id)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserName(String name)throws IOException, InterruptedException{
        List<User> allUsers = getAllUsers();
        return allUsers.stream()
                .filter(user -> user.getName().equals(name))
                .findAny()
                .orElse(new User());
    }

    public static String getAllComments(User user) throws IOException, InterruptedException{
        Post lastPost = getLastPostUser(user);
        String fileName = "user-" + user.getId() + "-post-" + lastPost.getId() + "-comments.json";

        HttpRequest requestForComments = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%d%s", DEFAULT_URI + POSTS, lastPost.getId(), COMMENTS)))
                .GET()
                .build();
        HttpResponse<Path> responseComments = CLIENT.send(requestForComments, HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));
        return "comments written to file " + responseComments.body();
    }
    private static Post getLastPostUser(User user) throws IOException, InterruptedException{
        HttpRequest requestForPosts = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", DEFAULT_URI, USERS, user.getId(), TODO)))
                .GET()
                .build();
        HttpResponse<String> responsePosts = CLIENT.send(requestForPosts, HttpResponse.BodyHandlers.ofString());
        List<Post> allUserPosts = GSON.fromJson(responsePosts.body(), new TypeToken<List<Post>>(){}.getType());
        return Collections.max(allUserPosts, Comparator.comparingInt(Post::getId));
    }
    public static List<Task> getListOfTasks(User user)throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", DEFAULT_URI, USERS, user.getId(), TODO)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> allTasks = GSON.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        return allTasks.stream()
                .filter(task -> !task.isComplete())
                .collect(Collectors.toList());
    }
}

