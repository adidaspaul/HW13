import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;


public class Main {

    public static final Gson GSON = new Gson();
    public static final int DEFAULT_USER_ID = 1;
    public static final String DEFAULT_USER_NAME = "John McLane";



    public static void main(String[] args) throws IOException, InterruptedException {
        User createUser = createDefaultUser();
        User createdUser = GSON.fromJson(HttpUtil.createUser(createUser), User.class);
        System.out.println("Created User -- " + createdUser);
        System.out.println("------------DIVIDER-----------");

        User updateUser = new User();
        updateUser.setName("Die Hard");
        updateUser.setEmail(createdUser.getEmail());
        updateUser.setWebsite(createdUser.getWebsite());
        updateUser.setAddress(createdUser.getAddress());
        updateUser.setPhone(createdUser.getPhone());
        updateUser.setCompany(createUser.getCompany());

        String str = HttpUtil.updateUser(DEFAULT_USER_ID, updateUser);
        User checkUpdate = GSON.fromJson(str, User.class);
        System.out.println(checkUpdate);
        System.out.println("------------DIVIDER-----------");

        createdUser.setId(DEFAULT_USER_ID);
        System.out.println("Deleted -> " + HttpUtil.deleteUser(createdUser));
        System.out.println("------------DIVIDER-----------");

        List<User> allUsers = HttpUtil.getAllUsers();
        allUsers.forEach(System.out::println);
        System.out.println("------------DIVIDER-----------");

        System.out.println("User with Id 1 is  " + HttpUtil.getUserId(DEFAULT_USER_ID));
        System.out.println("------------DIVIDER-----------");

        System.out.println("User name is " + HttpUtil.getUserName(DEFAULT_USER_NAME));
        System.out.println("------------DIVIDER-----------");

        System.out.println("Comments for user " + HttpUtil.getAllComments(createdUser));
        System.out.println("------------DIVIDER-----------");

        List<Task> allOpenTasks = HttpUtil.getListOfTasks(createdUser);
        allOpenTasks.forEach(System.out::println);
    }


    private static User createDefaultUser() {
        User user = new User();
        user.setId(DEFAULT_USER_ID);
        user.setName("Andrew");
        user.setUsername("Bishop");
        user.setAddress(defaultAddress());
        user.setEmail("Bishop@lambda.com");
        user.setPhone("0976-14124");
        user.setWebsite("www.abop.com");
        user.setCompany(defaultCompany());
        return user;
    }
    private static Address defaultAddress(){
        Address address = new Address();
        address.setStreet("Elms str");
        address.setSuite("13F");
        address.setCity("Idaho");
        address.setZipcode("1111");
        Geo geo = new Geo();
        geo.setLat(12);
        geo.setLon(123);
        address.setGeolocation(geo);
        return address;
    }

    private static Company defaultCompany(){
        Company company = new Company();
        company.setName("EvilCorp");
        company.setCatchPhrase("We are actually good");
        company.setBs("Supervision and Conquer");
        return company;
    }

}

