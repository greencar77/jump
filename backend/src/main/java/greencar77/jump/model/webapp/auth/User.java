package greencar77.jump.model.webapp.auth;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String name;
    private String password;
    private Set<Role> roles = new HashSet<>();

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void addRole(Role role) {
        //bind in both directions
        roles.add(role);
        role.addUser(this);
    }
    
    public User addRoles(Role... roles) {
        for (Role role: roles) {
            addRole(role);
        }
        
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
