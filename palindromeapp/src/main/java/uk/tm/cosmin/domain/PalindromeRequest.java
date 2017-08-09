package uk.tm.cosmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Cosmin on 7/25/2017.
 */
public class PalindromeRequest implements Serializable {

    //

    public PalindromeRequest() {
    }

    //

    @JsonIgnore
    private Date requestDate;

    @JsonProperty("palindrome")
    private String rawValue;

    @JsonIgnore
    private String value;

    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PalindromeRequest that = (PalindromeRequest) o;

        if (requestDate != null ? !requestDate.equals(that.requestDate) : that.requestDate != null) return false;
        if (rawValue != null ? !rawValue.equals(that.rawValue) : that.rawValue != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = requestDate != null ? requestDate.hashCode() : 0;
        result = 31 * result + (rawValue != null ? rawValue.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    //

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //

}
