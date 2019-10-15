package bean;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Table(name = "pms_product_sale_attr")
public class ProductSaleAttr implements Serializable {
    String id;
    String productId;
    String saleAttrId;
    String saleAttrName;
    @Transient
    List<ProductSaleAttrValue> spuSaleAttrValueList;

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSaleAttrId(String saleAttrId) {
        this.saleAttrId = saleAttrId;
    }

    public void setSaleAttrName(String saleAttrName) {
        this.saleAttrName = saleAttrName;
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

    public String getSaleAttrName() {
        return saleAttrName;
    }

    public void setSpuSaleAttrValueList(List<ProductSaleAttrValue> spuSaleAttrValueList) {
        this.spuSaleAttrValueList = spuSaleAttrValueList;
    }

    public List<ProductSaleAttrValue> getSpuSaleAttrValueList() {
        return spuSaleAttrValueList;
    }
}
