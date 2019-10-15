package bean;

import com.sun.xml.internal.ws.developer.Serialization;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "pms_base_catalog1")
public class BaseCatalog1 implements Serializable {
    String id;
    String name;
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
