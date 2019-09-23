package bean;

import java.io.Serializable;

public class SkuAttrValue implements Serializable {
    String id;
    String skuId;
    String attrId;
    String attrValue;

    public void setId(String id) {
        this.id = id;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

    public String getAttrValue() {
        return attrValue;
    }
}
