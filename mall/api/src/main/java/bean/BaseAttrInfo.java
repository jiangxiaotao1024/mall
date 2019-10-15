package bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "pms_base_attr_info")
public class BaseAttrInfo implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    @Column
    String attrName;
    @Column
    String catalog3Id;
    @Column
    String isEnabled;
    @Transient
    List<BaseAttrValue> attrValueList;

    public void setAttrValueList(List<BaseAttrValue> attrValueList) {
        this.attrValueList = attrValueList;
    }

    public List<BaseAttrValue> getAttrValueList() {
        return attrValueList;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getId() {
        return id;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }
}
