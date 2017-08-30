package com.jchanghong.appsearch.util;

import com.jchanghong.appsearch.constant.Constant;


public class CommonUtil {
    /**
     * get double compare  result
     *
     * @param result
     * @return
     */
    public static int compare(long result) {
        return (result > Constant.ZERO_OF_LONG) ? (1) : ((result < Constant.ZERO_OF_LONG) ? (-1) : (0));
    }

}

