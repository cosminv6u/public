package uk.tm.cosmin.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.tm.cosmin.Application;
import uk.tm.cosmin.domain.PalindromeRequest;

/**
 * Created by Cosmin on 7/25/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class PalindromeDAOImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeDAOImplTest.class);

    @InjectMocks
    private PalindromeDAOImpl palindromeDAO;

    @Before
    public void setUp() {
        PalindromeRequest request1 = new PalindromeRequest();
        PalindromeRequest request2 = new PalindromeRequest();
        palindromeDAO.save(request1);
        palindromeDAO.save(request2);
    }

    //

    @Test
    public void testSave() {
        PalindromeRequest palindromeRequest = new PalindromeRequest();

        palindromeDAO.save(palindromeRequest);

        Assert.assertEquals(3, palindromeDAO.findAllPalindromes(0, 100).size());
    }

    //

    @Test
    public void testFindAllPalindromesNegativeStart() {
        Assert.assertEquals(0, palindromeDAO.findAllPalindromes(-1, 10).size());
    }

    @Test
    public void testFindAllPalindromesOutOfBoundsStart() {
        Assert.assertEquals(0, palindromeDAO.findAllPalindromes(2, 10).size());
    }

    @Test
    public void testFindAllPalindromesNegativeRange() {
        Assert.assertEquals(0, palindromeDAO.findAllPalindromes(0, -1).size());
    }

    @Test
    public void testFindAllPalindromesOutOfRange() {
        Assert.assertEquals(1, palindromeDAO.findAllPalindromes(1, 2).size());
    }

    @Test
    public void testFindAllPalindromesOk() {
        Assert.assertEquals(2, palindromeDAO.findAllPalindromes(0, 2).size());
    }

    //

}
