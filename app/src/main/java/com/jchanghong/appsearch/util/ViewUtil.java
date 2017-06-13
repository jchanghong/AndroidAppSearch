package com.jchanghong.appsearch.util;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class ViewUtil {
    public static void showTextNormal(TextView tv, String text) {
        if ((null == tv) || (null == text)) {
            return;
        }

        tv.setText(text);
    }

    /**
     * @param tv
     * @param baseText
     * @param highlightText if the string of highlightText is a subset of the string of baseText,highlight the string of highlightText.
     */
    public static void showTextHighlight(TextView tv, String baseText, String highlightText) {
        if ((null == tv) || (null == baseText) || (null == highlightText)) {
            return;
        }

        int index = baseText.indexOf(highlightText);
        if (index < 0) {
            tv.setText(baseText);
            return;
        }

        int len = highlightText.length();
        StringBuilder builder = new StringBuilder(baseText.substring(0, index));
        builder.append("<font color=#FF8C00 >").append(baseText.substring(index, index + len)).append("</font>").append(baseText.substring(index + len, baseText.length()));
        Spanned spanned = Html.fromHtml(builder.toString(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV);

        tv.setText(spanned);
    }

    public static void showView(View view) {
        if (null == view) {
            return;
        }
        if (View.VISIBLE != view.getVisibility()) {
            view.setVisibility(View.VISIBLE);
        }
    }


}
