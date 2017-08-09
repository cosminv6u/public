package uk.tm.cosmin.repository;

import uk.tm.cosmin.domain.PalindromeRequest;

import java.util.List;

/**
 * Created by Cosmin on 7/25/2017.
 */
public interface PalindromeDAO {

    /**
     * Persists a palindromeRequest.
     *
     * @param palindromeRequest
     */
    void save(PalindromeRequest palindromeRequest);

    /**
     * Finds all PalindromeRequests that were really palindromes, ordered by Date DESC.
     * Supports pagination.
     *
     * @param startRange 0 based.
     * @param rangeSize
     * @return empty ist if range is out of bounds
     */
    List<PalindromeRequest> findAllPalindromes(Integer startRange, Integer rangeSize);
    // sort is hardcoded to date desc, but we could have used a SortCriteria object

}
