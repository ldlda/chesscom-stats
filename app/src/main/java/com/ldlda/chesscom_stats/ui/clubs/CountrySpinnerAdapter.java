package com.ldlda.chesscom_stats.ui.clubs;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.ldlda.chesscom_stats.util.Countries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountrySpinnerAdapter extends ArrayAdapter<String> {

    private final List<String> countryList;

    public CountrySpinnerAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_spinner_item, getCountryList());
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.countryList = getCountryList();
    }

    private static List<String> getCountryList() {
        List<String> list = new ArrayList<>();
        list.add("Country");
        list.addAll(Countries.COUNTRY_CODE.values());
        Collections.sort(list.subList(1, list.size()));
        return list;
    }

    public String getCountryCode(int position) {
        // Find ISO code by value
        String selectedCountry = countryList.get(position);
        for (var entry : Countries.COUNTRY_CODE.entrySet()) {
            if (entry.getValue().equals(selectedCountry)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
