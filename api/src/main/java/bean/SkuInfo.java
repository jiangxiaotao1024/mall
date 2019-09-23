package bean;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "sku_info")
public class SkuInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String productId;
    BigDecimal price;
    String skuName;
    BigDecimal weight;
    String skuDesc;
    String catalog3Id;
    String skuDefaultImg;
    @Transient
    List<SkuImage> skuImageList;
    @Transient
    List<SkuAttrValue> skuAttrValueList;
    @Transient
    List<SkuSaleAttrValue> skuSaleAttrValueList;

    public void setSkuAttrValueList(List<SkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }

    public void setSkuSaleAttrValueList(List<SkuSaleAttrValue> skuSaleAttrValueList) {
        this.skuSaleAttrValueList = skuSaleAttrValueList;
    }

    public List<SkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public List<SkuSaleAttrValue> getSkuSaleAttrValueList() {
        return skuSaleAttrValueList;
    }

    public void setSkuImageList(List<SkuImage> skuImageList) {
        this.skuImageList = skuImageList;
    }

    public List<SkuImage> getSkuImageList() {
        return skuImageList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public void setSkuDefaultImg(String skuDefaultImg) {
        this.skuDefaultImg = skuDefaultImg;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSkuName() {
        return skuName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public String getSkuDefaultImg() {
        return skuDefaultImg;
    }
}
