package uk.tm.cosmin.domain;

import java.io.Serializable;

/**
 * Created by Cosmin on 7/27/2017.
 */
public class PalindromeResponse implements Serializable {

    //

    public PalindromeResponse() {
    }

    public PalindromeResponse(boolean palindrome) {
        this.palindrome = palindrome;
    }

    //

    private Boolean palindrome;

    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PalindromeResponse that = (PalindromeResponse) o;

        return palindrome != null ? palindrome.equals(that.palindrome) : that.palindrome == null;
    }

    @Override
    public int hashCode() {
        return palindrome != null ? palindrome.hashCode() : 0;
    }

    //

    public Boolean getPalindrome() {
        return palindrome;
    }

    public void setPalindrome(Boolean palindrome) {
        this.palindrome = palindrome;
    }

    //

}
