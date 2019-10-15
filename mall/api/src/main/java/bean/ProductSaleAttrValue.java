package bean;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Table(name = "pms_product_sale_attr_value")
public class ProductSaleAttrValue implements Serializable {
    String id;
    String productId;
    String saleAttrId;
    String saleAttrValueName;
    @Transient
    String isChecked;
    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSaleAttrId(String saleAttrId) {
        this.saleAttrId = saleAttrId;
    }

    public void setSaleAttrValueName(String saleAttrValueName) {
        this.saleAttrValueName = saleAttrValueName;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getSaleAttrId() {
        return saleAttrId;
    }

    public String getSaleAttrValueName() {
        return saleAttrValueName;
    }
}
