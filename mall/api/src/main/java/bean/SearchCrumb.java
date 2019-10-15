package bean;

import java.io.Serializable;

public class SearchCrumb implements Serializable {
    String valueId;
    String valueName;
    String urlParam;

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }


    public String getValueId() {
        return valueId;
    }

    public String getValueName() {
        return valueName;
    }

}
