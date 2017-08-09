package uk.tm.cosmin.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.tm.cosmin.Application;

/**
 * Created by Cosmin on 7/26/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class PalindromeCheckerServiceImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeCheckerServiceImplTest.class);

    @InjectMocks
    private PalindromeCheckerServiceImpl palindromeCheckerService;

    //

    @Test
    public void testCheckIsPalindromeFalse() {
        Assert.assertFalse(palindromeCheckerService.checkIsPalindrome("cosmin"));
    }

    @Test
    public void testCheckIsPalindromeTrue() {
        Assert.assertTrue(palindromeCheckerService.checkIsPalindrome("131"));
    }

    //

}
