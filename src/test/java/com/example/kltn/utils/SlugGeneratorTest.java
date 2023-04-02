package com.example.kltn.utils;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class SlugGeneratorTest {
    @Test
    void slugify() throws UnsupportedEncodingException {
        String temp = "Điện thoại bố mệ anh chị em nhà tui";
//        System.out.println(SlugGenerator.slugify(temp));
        System.out.println(SlugGenerator.slugify(temp));

    }

}