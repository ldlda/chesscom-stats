package com.ldlda.chesscom_stats.ui.lessons.pager;

import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;

/**
 * Collection of gradient generation functions for lesson backgrounds.
 * Each method takes a base color and returns a styled GradientDrawable.
 * Hotswap these in LessonPagerContainerFragment.applyGradientBackground()
 */
public class GradientStyles {

    /**
     * Material "Illumination" Gradient - subtle hue drift with lighting depth
     * Best for: clean, modern UI with natural top-down lighting
     */
    public static GradientDrawable materialIllumination(@ColorInt int baseColor) {
        float[] hslTop = new float[3];
        float[] hslBottom = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslTop);
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslBottom);

        // TOP: Desaturated + lighter (soft illuminated surface)
        hslTop[1] = Math.max(0f, hslTop[1] * 0.7f);
        hslTop[2] = Math.min(1f, hslTop[2] + 0.15f * (1f - hslTop[2]));

        // BOTTOM: Subtle hue drift + deeper (shadow/depth)
        float hueShift = Math.max(15f, 30f * hslBottom[1]);
        hslBottom[0] = (hslBottom[0] + hueShift) % 360f;
        hslBottom[1] = Math.min(1f, hslBottom[1]);
        hslBottom[2] = Math.max(0f, hslBottom[2] - 0.05f * hslBottom[2]);

        int colorTop = androidx.core.graphics.ColorUtils.HSLToColor(hslTop);
        int colorBottom = androidx.core.graphics.ColorUtils.HSLToColor(hslBottom);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{colorTop, colorBottom}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }

    /**
     * Complementary Depth Gradient - high contrast, energetic
     * Best for: dynamic, eye-catching UI (Geometry Dash style)
     */
    public static GradientDrawable complementaryDepth(@ColorInt int baseColor) {
        float[] hslTop = new float[3];
        float[] hslBottom = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslTop);
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslBottom);

        // TOP: Slightly desaturated + lighter
        hslTop[1] = Math.max(0f, hslTop[1] * 0.8f);
        hslTop[2] = Math.min(1f, hslTop[2] + 0.1f);

        // BOTTOM: Complementary hue (180°) + vivid + darker
        hslBottom[0] = (hslBottom[0] + 180f) % 360f;
        hslBottom[1] = Math.min(1f, hslBottom[1] * 1.1f);
        hslBottom[2] = Math.max(0f, hslBottom[2] - 0.1f);

        int colorTop = androidx.core.graphics.ColorUtils.HSLToColor(hslTop);
        int colorBottom = androidx.core.graphics.ColorUtils.HSLToColor(hslBottom);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{colorTop, colorBottom}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }

    /**
     * Triadic Vibe Gradient - vibrant but balanced
     * Best for: game menus, colorful dashboards
     */
    public static GradientDrawable triadicVibe(@ColorInt int baseColor) {
        float[] hslTop = new float[3];
        float[] hslBottom = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslTop);
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslBottom);

        // TOP: Slightly desaturated + lighter
        hslTop[1] = Math.max(0f, hslTop[1] * 0.8f);
        hslTop[2] = Math.min(1f, hslTop[2] + 0.08f);

        // BOTTOM: Triadic hue (120°) + more saturated + darker
        hslBottom[0] = (hslBottom[0] + 120f) % 360f;
        hslBottom[1] = Math.min(1f, hslBottom[1] * 1.1f);
        hslBottom[2] = Math.max(0f, hslBottom[2] - 0.1f);

        int colorTop = androidx.core.graphics.ColorUtils.HSLToColor(hslTop);
        int colorBottom = androidx.core.graphics.ColorUtils.HSLToColor(hslBottom);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{colorTop, colorBottom}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }

    /**
     * Monochrome Depth - clean, minimal look
     * Best for: professional UI, readability-focused
     */
    public static GradientDrawable monochromeDepth(@ColorInt int baseColor) {
        float[] hslTop = new float[3];
        float[] hslBottom = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslTop);
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslBottom);

        // TOP: Much lighter (same hue, same saturation)
        hslTop[2] = Math.min(1f, hslTop[2] + 0.2f * (1f - hslTop[2]));

        // BOTTOM: Slightly darker (same hue, same saturation)
        hslBottom[2] = Math.max(0f, hslBottom[2] - 0.1f * hslBottom[2]);

        int colorTop = androidx.core.graphics.ColorUtils.HSLToColor(hslTop);
        int colorBottom = androidx.core.graphics.ColorUtils.HSLToColor(hslBottom);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{colorTop, colorBottom}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }

    /**
     * Analogous Harmony - smooth neighboring colors
     * Best for: cohesive, natural feeling gradients
     */
    public static GradientDrawable analogousHarmony(@ColorInt int baseColor) {
        float[] hslTop = new float[3];
        float[] hslBottom = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslTop);
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslBottom);

        // TOP: Slightly desaturated + lighter
        hslTop[1] = Math.max(0f, hslTop[1] * 0.75f);
        hslTop[2] = Math.min(1f, hslTop[2] + 0.12f * (1f - hslTop[2]));

        // BOTTOM: Small hue shift (30°) + more saturated + darker
        hslBottom[0] = (hslBottom[0] + 30f) % 360f;
        hslBottom[1] = Math.min(1f, hslBottom[1] * 1.2f);
        hslBottom[2] = Math.max(0f, hslBottom[2] - 0.15f * hslBottom[2]);

        int colorTop = androidx.core.graphics.ColorUtils.HSLToColor(hslTop);
        int colorBottom = androidx.core.graphics.ColorUtils.HSLToColor(hslBottom);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{colorTop, colorBottom}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }

    /**
     * Sunset Glow - warm, inviting gradient
     * Best for: warm colors (reds, oranges, yellows)
     */
    public static GradientDrawable sunsetGlow(@ColorInt int baseColor) {
        float[] hslTop = new float[3];
        float[] hslBottom = new float[3];
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslTop);
        androidx.core.graphics.ColorUtils.colorToHSL(baseColor, hslBottom);

        // TOP: Desaturated + very light (soft glow)
        hslTop[1] = Math.max(0f, hslTop[1] * 0.6f);
        hslTop[2] = Math.min(1f, hslTop[2] + 0.25f * (1f - hslTop[2]));

        // BOTTOM: Shift toward red/orange + saturated + darker
        hslBottom[0] = (hslBottom[0] - 15f + 360f) % 360f; // Shift toward warmer
        hslBottom[1] = Math.min(1f, hslBottom[1] * 1.3f);
        hslBottom[2] = Math.max(0f, hslBottom[2] - 0.2f * hslBottom[2]);

        int colorTop = androidx.core.graphics.ColorUtils.HSLToColor(hslTop);
        int colorBottom = androidx.core.graphics.ColorUtils.HSLToColor(hslBottom);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{colorTop, colorBottom}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }

    /**
     * Original RGB Multiplier - the first attempt (throws numbers until it works!)
     * Best for: nostalgia, remembering where we started
     */
    public static GradientDrawable originalRgbMultiplier(@ColorInt int baseColor) {
        // Just multiply RGB by 0.85f - simple but not color-theory-aware
        int r = Math.round(android.graphics.Color.red(baseColor) * 0.85f);
        int g = Math.round(android.graphics.Color.green(baseColor) * 0.85f);
        int b = Math.round(android.graphics.Color.blue(baseColor) * 0.85f);
        int darker = android.graphics.Color.rgb(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{baseColor, darker}
        );
        gradient.setCornerRadius(0);
        gradient.setAlpha(240);
        return gradient;
    }
}
