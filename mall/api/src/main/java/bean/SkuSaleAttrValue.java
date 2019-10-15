package bean;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "pms_sku_sale_attr_value")
public class SkuSaleAttrValue implements Serializable {
    String id;
    String skuId;
    String saleAttrId;
    String saleAttrValueId;
    String saleAttrName;
    String saleAttrValueName;

    public void setId(String id) {
        this.id = id;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public void setSaleAttrId(String saleAttrId) {
        this.saleAttrId = saleAttrId;
    }

    public void setSaleAttrValueId(String saleAttrValueId) {
        this.saleAttrValueId = saleAttrValueId;
    }

    public void setSaleAttrName(String saleAttrName) {
        this.saleAttrName = saleAttrName;
    }

    public void setSaleAttrValueName(String saleAttrValueName) {
        this.saleAttrValueName = saleAttrValueName;
    }

    public String getId() {
        return id;
    }

    public String getSkuId() {
        return skuId;
    }

    public String getSaleAttrId() {
        return saleAttrId;
    }

    public String getSaleAttrValueId() {
        return saleAttrValueId;
    }

    public String getSaleAttrName() {
        return saleAttrName;
    }

    public String getSaleAttrValueName() {
        return saleAttrValueName;
    }
}
