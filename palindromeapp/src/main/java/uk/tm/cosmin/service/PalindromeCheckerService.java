package uk.tm.cosmin.service;

/**
 * Created by Cosmin on 7/25/2017.
 */
public interface PalindromeCheckerService {

    /**
     * Checks if a certain value is a palindrome.
     * @param value
     * @return
     */ // might connect to a remote service
    boolean checkIsPalindrome(String value);

}
