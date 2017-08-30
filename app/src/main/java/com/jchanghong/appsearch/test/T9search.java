package com.jchanghong.appsearch.test;

import com.pinyinsearch.model.PinyinSearchUnit;
import com.pinyinsearch.model.PinyinUnit;
import com.pinyinsearch.util.PinyinUtil;
import com.pinyinsearch.util.T9Util;

/**
 * Created by jiang on 2017/8/30 0030.
 */

public class T9search {
    public static void main(String[] args) {
        PinyinSearchUnit unit = new PinyinSearchUnit("clock");
        System.out.println(T9Util.getT9Number('c'));
        System.out.println(T9Util.getT9Number('d'));
        PinyinUtil.parse(unit);

        System.out.println(PinyinUtil.getFirstCharacter(unit));
        System.out.println(PinyinUtil.getFirstLetter(unit));
        System.out.println(PinyinUtil.getSortKey(unit));


    }
}
