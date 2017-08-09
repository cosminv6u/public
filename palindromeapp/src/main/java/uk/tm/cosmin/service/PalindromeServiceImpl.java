package uk.tm.cosmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.tm.cosmin.domain.PalindromeRequest;
import uk.tm.cosmin.domain.PalindromeResponse;
import uk.tm.cosmin.repository.PalindromeDAO;

import java.util.List;

/**
 * Created by Cosmin on 7/25/2017.
 */
@Service
// @Transactional // depending on the DS
public class PalindromeServiceImpl implements PalindromeService {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeServiceImpl.class);

    // we remove any non alphanumeric characters! (even diacritics like é, à)
    private static final String PROCESS_PATTERN = "[^A-Za-z0-9]";

    @Autowired
    private PalindromeCheckerService palindromeCheckerService;

    @Autowired
    private PalindromeDAO palindromeDAO;

    //

    @Override
    public List<PalindromeRequest> getPalindromeRequests() {
        return palindromeDAO.findAllPalindromes(0, 10);
    }

    @Override // we don't cache this because we need to also save the request
    public PalindromeResponse checkPalindrome(PalindromeRequest palindromeRequest) {
        validate(palindromeRequest);

        processRequest(palindromeRequest);

        LOG.info("calling checkIsPalindrome!");
        boolean isPalindrome = palindromeCheckerService.checkIsPalindrome(palindromeRequest.getValue());
        // if logic would have been more complex, maybe we needed to call a remote service

        if (isPalindrome) {
            palindromeDAO.save(palindromeRequest);
        }

        return new PalindromeResponse(isPalindrome);
    }

    //

    public PalindromeRequest processRequest(PalindromeRequest palindromeRequest) {
        palindromeRequest.setValue(palindromeRequest.getRawValue().replaceAll(PROCESS_PATTERN, "").toLowerCase());
        return palindromeRequest;
    }

    public void validate(PalindromeRequest palindromeRequest) {
        if (palindromeRequest == null || palindromeRequest.getRawValue() == null) {
            throw new IllegalArgumentException("param can't be null!");
        }
    }

    //

}
