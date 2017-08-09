package uk.tm.cosmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.cache.annotation.CacheResult;

/**
 * Created by Cosmin on 7/25/2017.
 */
@Service
public class PalindromeCheckerServiceImpl implements PalindromeCheckerService {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeCheckerServiceImpl.class);

    @CacheResult // cache result if it takes too long
    public boolean checkIsPalindrome(String value) {
        LOG.info("called checkIsPalindrome!!!");
        // compute here or even call external service
        return value.equals(new StringBuilder(value).reverse().toString());
    }

}
