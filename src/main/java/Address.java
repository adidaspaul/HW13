import lombok.Data;



@Data
public class Address {
    private String street, suite , city, zipcode;
    private Geo geolocation;

}
