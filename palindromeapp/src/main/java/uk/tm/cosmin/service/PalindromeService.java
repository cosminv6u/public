package uk.tm.cosmin.service;

import uk.tm.cosmin.domain.PalindromeRequest;
import uk.tm.cosmin.domain.PalindromeResponse;

import java.util.List;

/**
 * Created by Cosmin on 7/25/2017.
 */
public interface PalindromeService {

    /**
     * Returns the last 10 palindrome requests that were really palindromes (even if duplicated!)
     * ordered by requested date DESC
     *
     * @return
     */
    List<PalindromeRequest> getPalindromeRequests();

    /**
     * Checks if the request is a palindrome ignoring whitespace and punctuation.
     * Example: 1331, Dammit I'm Mad, etc.
     *
     * @param palindromeRequest
     * @return
     * @throws IllegalArgumentException if palindromeRequest or palindromeRequest.getRawValue is null
     */
    PalindromeResponse checkPalindrome(PalindromeRequest palindromeRequest);

}
