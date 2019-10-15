package bean;


import com.sun.xml.internal.ws.developer.Serialization;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

public class SearchParam implements Serializable {
    String catalog3Id;
    String keyword;
    String[] valueId;

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public String getCatalog3Id() {
        return catalog3Id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setValueId(String[] valueId) {
        this.valueId = valueId;
    }

    public String[] getValueId() {
        return valueId;
    }
}
