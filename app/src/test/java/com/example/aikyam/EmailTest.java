package com.example.aikyam;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailTest {

    @Test
    public void validateTrue() {
        assertTrue(Utility.validate("shardul.lingwal@iiitb.org"));
    }


    @Test
    public void isNotNull() {
        assertTrue(Utility.validate("abc@d.com"));
    }


}