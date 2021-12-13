import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.Objects;

@AllArgsConstructor
@Data


public class User {

    private int id;
    private String name, username, email, phone, website;
    private Address address;
    private Geo geolocation;
    private Company company;

    public User(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name)
                && Objects.equals(username, user.username)
                && Objects.equals(email, user.email)
                && Objects.equals(address, user.address)
                && Objects.equals(phone, user.phone)
                && Objects.equals(website, user.website)
                && Objects.equals(address, user.address)
                && Objects.equals(geolocation, user.geolocation)
                && Objects.equals(company, user.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, email, address, phone, website, address, geolocation, company);
    }
}
