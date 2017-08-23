package greencar77.jump.model.webapp.auth;

import java.util.HashSet;
import java.util.Set;

public class AuthRealm {
    private Set<User> users = new HashSet<>();
    private Set<Role> roles = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
