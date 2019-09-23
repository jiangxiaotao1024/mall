package bean;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "product_sale_attr_value")
public class ProductSaleAttrValue implements Serializable {
    String id;
    String productId;
    String saleAttrId;
    String saleAttrValueName;

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
