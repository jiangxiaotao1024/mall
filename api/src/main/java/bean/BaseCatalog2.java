package bean;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "base_catalog2")
public class BaseCatalog2 implements Serializable {
    String id;
    String name;
    String catalog1Id;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCatalog1Id(String catalog1Id) {
        this.catalog1Id = catalog1Id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCatalog1Id() {
        return catalog1Id;
    }
}
