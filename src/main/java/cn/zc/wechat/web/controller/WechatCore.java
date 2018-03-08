package cn.zc.wechat.web.controller;

import cn.zc.wechat.dto.Signature;
import cn.zc.wechat.util.CryptoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class WechatCore {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatCore.class);

    @RequestMapping(value = "core", method = RequestMethod.GET)
    public String core(Signature signature) {
        LOGGER.info(signature.toString());
        try {
            if (checkSignature(signature)) {
                return signature.getEchostr();
            } else {
                LOGGER.info("非法请求");
                return null;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return null;
        }
    }

    private boolean checkSignature(Signature signature) {
        String[] arr = new String[]{signature.getNonce(), signature.getTimestamp(), signature.getToken()};
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s);
        }
        String sha1 = CryptoUtils.getSHA1(sb.toString());
        return signature.getSignature().equals(sha1);
    }

}
