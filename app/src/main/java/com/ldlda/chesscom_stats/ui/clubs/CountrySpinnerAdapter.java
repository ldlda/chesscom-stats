package com.ldlda.chesscom_stats.ui.clubs;

import static com.ldlda.chesscom_stats.util.Countries.COUNTRY_CODE;

import android.content.Context;
import android.util.Pair;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class CountrySpinnerAdapter extends ArrayAdapter<String> {
    private static final List<Pair<String, String>> countryMap = low();

    public CountrySpinnerAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_spinner_item, getCountryList());
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private static List<Pair<String, String>> low() {
        return COUNTRY_CODE.entrySet().stream()
                .map(e -> Pair.create(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(o -> o.second))
                .toList();
    }

    private static List<String> getCountryList() {
        return Stream.concat(
                Stream.of("Country"),
                countryMap.stream().map(s -> s.second)
        ).toList();
    }

    public String getCountryCode(int position) {
        // Find ISO code by value
        if (position == 0) return null;
        return countryMap.get(position - 1).first;
    }
}
