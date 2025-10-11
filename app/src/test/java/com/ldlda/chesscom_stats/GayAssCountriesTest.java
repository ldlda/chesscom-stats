package com.ldlda.chesscom_stats;

import static com.ldlda.chesscom_stats.util.Countries.COUNTRY_CODE;
import static org.junit.Assert.assertTrue;
import static java.util.Locale.IsoCountryCode.PART1_ALPHA2;

import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GayAssCountriesTest {
    @Test
    public void test() {
        HashMap<String, String> copy = new HashMap<>(Map.copyOf(COUNTRY_CODE));
        Locale.getISOCountries(PART1_ALPHA2).stream().sorted().forEach(i -> {
            System.out.format("java: %s: %s\n", i, new Locale("", i).getDisplayName());
            System.out.format("ours: %s: %s\n\n", i, copy.remove(i));
        });
        System.out.println("we still have:");
        for (var i : copy.keySet()) {
            System.out.format("%s: %s\n\n", i, copy.get(i));
            assertTrue(String.valueOf(i.charAt(0)).equalsIgnoreCase("X"));
        }
    }
}
