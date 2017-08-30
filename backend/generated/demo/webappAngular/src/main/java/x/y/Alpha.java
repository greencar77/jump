package x.y;

import java.util.Arrays;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import x.y.User;

@Path("/user")
public class Alpha  {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        return user;

    }
}
