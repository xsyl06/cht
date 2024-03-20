/*
 * MIT License
 * Copyright 2024-present cht
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cht.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.cht.mp.pojo.database.DictDto;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SMUtils {
    @Autowired
    private DictUtils dictUtils;
    public String decryptStr(String data) {
        DictDto keyPair = dictUtils.getKeyPair();
        byte[] dataBytes = HexUtil.decodeHex(data);
        SM2 sm2_1 = SmUtil.sm2(keyPair.getDictValue2(), null);
        byte[] decrypt = sm2_1.decrypt(dataBytes, KeyType.PrivateKey);
        return new String(decrypt);
    }

    public String encryptStr(String data) {
        DictDto keyPair = dictUtils.getKeyPair();
        byte[] dateByte = data.getBytes(StandardCharsets.UTF_8);
        SM2 sm2 = SmUtil.sm2(null, keyPair.getDictValue4());
        byte[] encrypt = sm2.encrypt(dateByte, KeyType.PublicKey);
        return HexUtil.encodeHexStr(encrypt);
    }

    public static void main(String[] args) {
        SM2 sm2 = SmUtil.sm2();
        byte[] privateKey = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        byte[] publicKey = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
        String privateKeyStr = HexUtil.encodeHexStr(privateKey);
        String publicKeyStr = HexUtil.encodeHexStr(publicKey);
        System.out.println("私钥: " + privateKeyStr);
        System.out.println("公钥: " + publicKeyStr);
        System.out.println("===============================");
        //生成盐（如果对象中没有，则新增一个16位的盐，如果有，使用对象中的）
        String tmpStr = "张三";
        SM2 sm2_1 = SmUtil.sm2(privateKeyStr, publicKeyStr);
        byte[] signByte = sm2_1.sign(tmpStr.getBytes(StandardCharsets.UTF_8));
        String sign = HexUtil.encodeHexStr(signByte);
        System.out.println("加密后密码："+sign);
        System.out.println("校验密码："+sm2.verify(tmpStr.getBytes(StandardCharsets.UTF_8), HexUtil.decodeHex(sign)));
        System.out.println("===============================");
        String dataStr = "测试加解密报文";
        byte[] dateByte = dataStr.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = sm2_1.encrypt(dateByte, KeyType.PublicKey);
        String encodeStr = HexUtil.encodeHexStr(encrypt);
        System.out.println("前后端加密后密文："+encodeStr);
        byte[] dataBytes = HexUtil.decodeHex(encodeStr);
        byte[] decrypt = sm2_1.decrypt(dataBytes, KeyType.PrivateKey);
        System.out.println("前后端加密后解密明文："+new String(decrypt));
        System.out.println("===============================");
        String adminSalt = "skwvt3uwxpxcyaoo";
        String pwdStr = "张三"+adminSalt;
        SM2 sm2_2 = SmUtil.sm2(privateKeyStr, publicKeyStr);
        byte[] pwdSignByte = sm2_2.sign(pwdStr.getBytes(StandardCharsets.UTF_8));
        String pwdSign = HexUtil.encodeHexStr(pwdSignByte);
        System.out.println("加密后密码："+pwdSign);





    }
}
