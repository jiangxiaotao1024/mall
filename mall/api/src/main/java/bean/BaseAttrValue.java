package bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "pms_base_attr_value")
public class BaseAttrValue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String valueName;
    String attrId;

    public void setId(String id) {
        this.id = id;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getId() {
        return id;
    }

    public String getValueName() {
        return valueName;
    }

    public String getAttrId() {
        return attrId;
    }
}
