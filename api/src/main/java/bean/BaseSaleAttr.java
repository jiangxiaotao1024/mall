package bean;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "base_sale_attr")
public class BaseSaleAttr implements Serializable {
    String id;
    String name;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
