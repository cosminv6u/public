package uk.tm.cosmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tm.cosmin.domain.PalindromeFault;
import uk.tm.cosmin.domain.PalindromeRequest;
import uk.tm.cosmin.domain.PalindromeResponse;
import uk.tm.cosmin.service.PalindromeService;

import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Cosmin on 7/25/2017.
 */
@RestController
@RequestMapping("/v1")
public class PalindromeController {

    @Autowired
    private PalindromeService palindromeService;

    // GET, even if it's not truly idempotent!
    @RequestMapping(value = "/checkPalindrome/{value}", method = GET)
    public PalindromeResponse checkPalindrome(@PathVariable String value) {
        PalindromeRequest palindromeRequest = new PalindromeRequest();
        palindromeRequest.setRawValue(value);
        palindromeRequest.setRequestDate(new Date());

        return palindromeService.checkPalindrome(palindromeRequest);
    }

    @RequestMapping(value = "/getPalindromes", method = GET)
    public List<PalindromeRequest> getPalindromes() {
        return palindromeService.getPalindromeRequests();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<PalindromeFault> handleException(Throwable er) {
        HttpHeaders responseHeaders = new HttpHeaders();

        PalindromeFault PalindromeFault = new PalindromeFault();
        PalindromeFault.setCode("0001"); // in the future many codes can be provided
        PalindromeFault.setMessage(er.getMessage());
        PalindromeFault.setDescription("Error: " + er.getClass().getName());

        return new ResponseEntity<PalindromeFault>(PalindromeFault, responseHeaders, HttpStatus.BAD_REQUEST);
    }
}
