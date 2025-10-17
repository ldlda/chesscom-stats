package com.ldlda.chesscom_stats.ui.playersearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.ldlda.chesscom_stats.R;

public class PlayerProfileWebView extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_puzzle_solve, container, false);

        WebView webView = view.findViewById(R.id.puzzle_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        if (getArguments() != null) {
            String url = getArguments().getString("profile_url");
            webView.loadUrl(url);
        }

        return view;
    }
}