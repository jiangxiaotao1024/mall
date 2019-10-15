package bean;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "pms_sku_attr_value")
public class SkuAttrValue implements Serializable {
    String id;
    String skuId;
    String attrId;
    String valueId;

    public void setId(String id) {
        this.id = id;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }


    public String getId() {
        return id;
    }

    public String getSkuId() {
        return skuId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueId() {
        return valueId;
    }
}
