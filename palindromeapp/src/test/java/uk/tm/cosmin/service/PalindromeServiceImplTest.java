package uk.tm.cosmin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.tm.cosmin.Application;
import uk.tm.cosmin.domain.PalindromeRequest;
import uk.tm.cosmin.domain.PalindromeResponse;
import uk.tm.cosmin.repository.PalindromeDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Cosmin on 7/25/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class PalindromeServiceImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeServiceImplTest.class);

    @Mock
    private PalindromeDAO palindromeDAO;

    @Mock
    private PalindromeCheckerService palindromeCheckerService;

    @InjectMocks
    private PalindromeServiceImpl palindromeService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    //

    @Test
    public void testGetPalindromeRequests() {
        List<PalindromeRequest> palindromeRequests = new ArrayList<PalindromeRequest>(1);
        PalindromeRequest request1 = new PalindromeRequest();
        request1.setRawValue("I'm a mi");
        request1.setValue("imami");
        request1.setRequestDate(new Date());
        palindromeRequests.add(request1);

        when(palindromeDAO.findAllPalindromes(0, 10)).thenReturn(palindromeRequests);

        List<PalindromeRequest> palindromeRequestList = palindromeService.getPalindromeRequests();

        assertEquals(1, palindromeRequestList.size());
        assertEquals("I'm a mi", palindromeRequestList.get(0).getRawValue());
        assertEquals("imami", palindromeRequestList.get(0).getValue());
        assertNotNull(palindromeRequestList.get(0).getRequestDate());
    }

    //

    @Test
    public void testCheckPalindromeFalse() {
        PalindromeRequest request1 = new PalindromeRequest();
        request1.setRawValue("cosmin");

        when(palindromeCheckerService.checkIsPalindrome("cosmin")).thenReturn(false);

        PalindromeResponse response = palindromeService.checkPalindrome(request1);

        assertNotNull(response);
        assertFalse(response.getPalindrome());
        verify(palindromeCheckerService).checkIsPalindrome("cosmin");
    }

    @Test
    public void testCheckPalindromeTrue() {
        PalindromeRequest request1 = new PalindromeRequest();
        request1.setRawValue("I'm a mi");

        when(palindromeCheckerService.checkIsPalindrome("imami")).thenReturn(true);

        PalindromeResponse response = palindromeService.checkPalindrome(request1);

        assertNotNull(response);
        assertTrue(response.getPalindrome());
        verify(palindromeCheckerService).checkIsPalindrome("imami");
        ArgumentCaptor<PalindromeRequest> savedCaptor = ArgumentCaptor.forClass(PalindromeRequest.class);
        verify(palindromeDAO).save(savedCaptor.capture());
        assertEquals("I'm a mi", savedCaptor.getValue().getRawValue());
        assertEquals("imami", savedCaptor.getValue().getValue());
    }

    //

    @Test
    public void testProcessRequestOk() {
        PalindromeRequest request1 = new PalindromeRequest();
        request1.setRawValue("I'm a mi");

        PalindromeRequest result = palindromeService.processRequest(request1);

        assertEquals("I'm a mi", result.getRawValue());
        assertEquals("imami", result.getValue());
    }

    //

    @Test
    public void testValidateNull() {
        try {
            palindromeService.validate(null);
            fail("Exception should be thrown.");
        } catch (IllegalArgumentException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testValidateEmpty() {
        PalindromeRequest request1 = new PalindromeRequest();

        try {
            palindromeService.validate(request1);
            fail("Exception should be thrown.");
        } catch (IllegalArgumentException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testValidateOk() {
        PalindromeRequest request1 = new PalindromeRequest();
        request1.setRawValue("doesn't matter");

        palindromeService.validate(request1);
    }

    //
}
