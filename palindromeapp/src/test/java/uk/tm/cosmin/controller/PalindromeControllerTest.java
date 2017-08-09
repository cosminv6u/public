package uk.tm.cosmin.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.tm.cosmin.domain.PalindromeRequest;
import uk.tm.cosmin.domain.PalindromeResponse;
import uk.tm.cosmin.service.PalindromeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Cosmin on 7/28/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PalindromeController.class)
public class PalindromeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PalindromeService palindromeService;

    //

    @Test
    public void getPalindromesTestOk() throws Exception {
        List<PalindromeRequest> palindromeRequestList = new ArrayList<>();
        PalindromeRequest palindromeRequest = new PalindromeRequest();
        palindromeRequest.setRawValue("aaa");
        palindromeRequestList.add(palindromeRequest);

        when(palindromeService.getPalindromeRequests()).thenReturn(palindromeRequestList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/getPalindromes");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].palindrome", is("aaa")));

        verify(palindromeService, times(1)).getPalindromeRequests();
    }

    @Test
    public void getPalindromesTestNull() throws Exception {
        when(palindromeService.getPalindromeRequests()).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/getPalindromes");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(palindromeService, times(1)).getPalindromeRequests();
    }

    @Test
    public void getPalindromesTestEmpty() throws Exception {
        when(palindromeService.getPalindromeRequests()).thenReturn(Collections.<PalindromeRequest>emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/getPalindromes");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[]"));

        verify(palindromeService, times(1)).getPalindromeRequests();
    }

    //

    @Test
    public void checkPalindromeTest() throws Exception {
        PalindromeResponse palindromeResponse = new PalindromeResponse();
        palindromeResponse.setPalindrome(Boolean.TRUE); // true or false, the test is the same
        when(palindromeService.checkPalindrome(isA(PalindromeRequest.class))).thenReturn(palindromeResponse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/checkPalindrome/131");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.palindrome", is(Boolean.TRUE)));

        verify(palindromeService, times(1)).checkPalindrome(isA(PalindromeRequest.class));
    }

    //

    @Test
    public void handleExceptionTest() throws Exception {
        // doesn't matter what method we test
        when(palindromeService.checkPalindrome(isA(PalindromeRequest.class))).thenThrow(new RuntimeException("cosmin"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/checkPalindrome/131");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("0001")))
                .andExpect(jsonPath("$.message", is("cosmin")))
                .andExpect(jsonPath("$.description", is("Error: java.lang.RuntimeException")));

        verify(palindromeService, times(1)).checkPalindrome(isA(PalindromeRequest.class));
    }

    //

    @Test
    public void badUrlTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/blabla");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError());
    }

    //

}
