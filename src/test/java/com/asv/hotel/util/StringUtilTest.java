package com.asv.hotel.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void testConvertStringToBoolean(){
        String booleanLine=" True";
        String booleanLine2="fAlSe ";
        String booleanLine3=";lsldf";
        assertTrue(StringUtil.convertStringToBoolean(booleanLine));
        assertFalse(StringUtil.convertStringToBoolean(booleanLine2));
        assertNull(StringUtil.convertStringToBoolean(booleanLine3));
    }

}