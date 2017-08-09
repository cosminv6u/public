package uk.tm.cosmin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Cosmin on 7/27/2017.
 */
@RestController
public class RootController {

    @RequestMapping("/**")
    public String help() {
        return "Methods supported= GET /v1/checkPalindrome/:toCheck , GET /v1/getPalindromes";
    }

}
