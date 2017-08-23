package greencar77.jump.model.webapp.auth;

import java.util.HashSet;
import java.util.Set;

public class Role {
    private String name;
    private Set<User> users = new HashSet<>();

    public Role(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    protected void addUser(User user) {
        users.add(user);
    }
}
