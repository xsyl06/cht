package com.cht.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.cht.mp.pojo.database.DictDto;
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
/*
//        SM2 sm2 = SmUtil.sm2();
//        byte[] privateKey = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
//        byte[] publicKey = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
//        System.out.println("私钥: " + HexUtil.encodeHexStr(privateKey));
//        System.out.println("公钥: " + HexUtil.encodeHexStr(publicKey));

//        String text = "张三";
//        byte[] dateByte = text.getBytes(StandardCharsets.UTF_8);
//        SM2 sm2 = SmUtil.sm2(null, publicKey);
//        byte[] encrypt = sm2.encrypt(dateByte, KeyType.PublicKey);
//        String e = HexUtil.encodeHexStr(encrypt);
//        System.out.println(e);
//        String b = "0499b246adb0a3da1b7eabe2fc3661263da37fadfe7b1f84e327bd7345e26249fee343a27cb8791aae77a92bce7fc8bb1b7550f4d191da0a4dbb3a9d80fe16c36cbf8ed5e3a02af2f6ec6cdcdcc6694994e6da4adf6f2e38ceeda85bb1951eb550416c1884b4f4e1089c2ed0be1b186617f682e3f62f0c8e1047b714e234d34d5d565ca79d6310e379eb14";
//        byte[] eBytes = HexUtil.decodeHex(b);
//        SM2 sm2_1 = SmUtil.sm2(privateKey, null);
//        byte[] decrypt = sm2_1.decrypt(eBytes, KeyType.PrivateKey);
//        System.out.println(new String(decrypt));*/
        String publicKey = "04c44236f742a4c98caea8e70b4f11d30ea391616f7018fa04d753e0849ca38fc06e70a4dff1b752f20605d866f4d14953d667b3c73506610043b83ad06a531cee";
        String privateKey = "6b8c0f237c6f97c82682c0eddab082080cc909a8b6e8735eac874a59c2962658";
        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        String a = "admin123skwvt3uwxpxcyaoo";
        byte[] sign = sm2.sign(a.getBytes(StandardCharsets.UTF_8));
        String b = HexUtil.encodeHexStr(sign);
        System.out.println(b);
        System.out.println("===========");

        SM2 sm2_1 = SmUtil.sm2(null, publicKey);

        boolean verify = sm2_1.verify(a.getBytes(StandardCharsets.UTF_8), HexUtil.decodeHex(b));
        System.out.println(verify);



    }
}
