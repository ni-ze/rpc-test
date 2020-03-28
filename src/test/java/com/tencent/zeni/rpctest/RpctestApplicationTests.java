package com.tencent.zeni.rpctest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class RpctestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1() {
        String temp = "[unisearch][2020-03-27 17:34:22][com.tencent.gov.goff.common.service.filter.XssHttpServletRequestWrapper->inputHandlers#161][INFO][X4z7lL7H57]: ......";
        String temp2 = "[INTEL-CITY-META][2020-03-28 11:49:26][com.tencent.gov.goff.common.service.filter.XssHttpServletRequestWrapper->inputHandlers#161][INFO][UPOpuBHlU8]: 请求参数:";

        Pattern pattern = Pattern.compile("^\\[.+\\]\\[([0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2})\\]");

        Matcher matcher = pattern.matcher(temp2);

        if (matcher.find()) {
            System.out.println("find");
        } else {
            System.out.println("no find");
        }
    }
}
