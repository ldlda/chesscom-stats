package com.ldlda.chesscom_stats.ui.puzzle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ldlda.chesscom_stats.R;

public class PuzzleSolve extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_puzzle_solve, container, false);

        WebView webView = view.findViewById(R.id.puzzle_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        if (getArguments() != null) {
            String url = getArguments().getString("puzzle_url");
            webView.loadUrl(url);
        }

        return view;
    }
}