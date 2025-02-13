package org.chomookun.arch4j.core.common.support;

import org.chomookun.arch4j.core.common.cli.InteractiveUtil;

import java.util.LinkedHashMap;
import java.util.Map;

class InteractiveUtilsTest {

    public static void main(String[] args) {
        Map<String,String> selectMap = new LinkedHashMap<>(){{
            put("I", "(Re)Install");
            put("U", "Update");
        }};
        String answer = InteractiveUtil.askSelect("select", selectMap);
        System.out.println("answer:" + answer);
    }

}