package com.youloft.webview;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

public class ProtocolDispatcherTest {

    @Test
    public void testUrlMap(){

        String[] testUrl = new String[]{
                "protocol://share#{xx:xx}",
                "protocol://auth:{xx:11}",
                "protocol://copytext:fuck..",
                "protocol://reportadclick#1#23#22#4",
                "protocol://repo:",
                "protocol://repo?xx=1&xx=2&xx=3",
                "protocol://haha?<p></p>"
        };

        for (String s : testUrl) {
            JSONObject jsonObject = ProtocolDispatcher.parseUrl2Cmd(s, "");
            System.out.println("parse:"+jsonObject.toJSONString());
        }



    }

}