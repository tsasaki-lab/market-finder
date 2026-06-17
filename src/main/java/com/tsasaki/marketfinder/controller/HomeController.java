package com.tsasaki.marketfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップページを表示するControllerです。
 */
@Controller
public class HomeController {

    /**
     * Market Finderのトップページを表示します。
     *
     * @return トップページのテンプレート名
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
