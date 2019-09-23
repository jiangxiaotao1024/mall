package bean;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "base_catalog3")
public class BaseCatalog3 implements Serializable {
    String id;
    String name;
    String catalog2Id;
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

    public String getCatalog2Id() {
        return catalog2Id;
    }

    public void setCatalog2Id(String catalog2Id) {
        this.catalog2Id = catalog2Id;
    }
}
