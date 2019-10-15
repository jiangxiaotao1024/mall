package bean;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "pms_sku_info")
public class SkuInfo implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    String id;
    @Column
    String spuId;
    @Column
    BigDecimal price;
    @Column
    String skuName;
    @Column
    BigDecimal weight;
    @Column
    String skuDesc;
    @Column
    String catalog3Id;
    @Column
    String skuDefaultImg;
    @Transient
    List<SkuImage> skuImageList;
    @Transient
    List<SkuAttrValue> skuAttrValueList;

    public void setId(String id) {
        this.id = id;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
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

    public void setSkuImageList(List<SkuImage> skuImageList) {
        this.skuImageList = skuImageList;
    }

    public void setSkuAttrValueList(List<SkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }

    public void setSkuSaleAttrValueList(List<SkuSaleAttrValue> skuSaleAttrValueList) {
        this.skuSaleAttrValueList = skuSaleAttrValueList;
    }

    public String getId() {
        return id;
    }

    public String getSpuId() {
        return spuId;
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

    public List<SkuImage> getSkuImageList() {
        return skuImageList;
    }

    public List<SkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public List<SkuSaleAttrValue> getSkuSaleAttrValueList() {
        return skuSaleAttrValueList;
    }

    @Transient
    List<SkuSaleAttrValue> skuSaleAttrValueList;
}