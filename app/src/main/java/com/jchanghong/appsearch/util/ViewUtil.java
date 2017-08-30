package com.jchanghong.appsearch.util;

import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class ViewUtil {

    /**
     * @param tv
     * @param baseText
     * @param highlightText if the string of highlightText is a subset of the string of baseText,highlight the string of highlightText.
     */
    public static void showTextHighlight(TextView tv, String baseText, String highlightText) {
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

}
