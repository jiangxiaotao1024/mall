package bean;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "user")
public class User implements Serializable {
    int id;
    String name;
    String password;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
