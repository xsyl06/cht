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

import cn.hutool.core.map.MapUtil;
import com.cht.admin.mapper.WrapperFactory;
import com.cht.enums.DictEnums;
import com.cht.mp.mapper.DictMapper;
import com.cht.mp.pojo.database.DictDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class DictUtils {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    DictMapper dictMapper;
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";

    private static final String H5_PUBLIC_KEY = "h5PublicKey";

    public DictDto getKeyPair() {
        DictDto dictDto;
        Map<Object, Object> fromHash = redisUtils.getFromHash(redisUtils.getDictKey(DictEnums.SYS_KEY_PAIR.getDictType()));
        if (MapUtil.isNotEmpty(fromHash)) {
            //缓存中存在密钥对
            dictDto = new DictDto();
            dictDto.setDictValue3(MapUtil.getStr(fromHash, PUBLIC_KEY));
            dictDto.setDictValue2(MapUtil.getStr(fromHash, PRIVATE_KEY));
            dictDto.setDictValue4(MapUtil.getStr(fromHash, H5_PUBLIC_KEY));
        } else {
            dictDto = dictMapper.selectOne(
                    WrapperFactory.queryDict(
                            DictEnums.SYS_KEY_PAIR.getDictType(),
                            DictEnums.SYS_KEY_PAIR.getDictValue()));
            saveRedisCatch(dictDto);
        }
        return dictDto;
    }

    /**
     * 将数据字典中的keyPair保存到redis缓存中
     *
     * @param dictDto 字典项目，其中公钥存在value3中，私钥存在value2中
     */
    private void saveRedisCatch(DictDto dictDto) {
        redisUtils.addToHash(redisUtils.getDictKey(DictEnums.SYS_KEY_PAIR.getDictType()), PUBLIC_KEY, dictDto.getDictValue3());
        redisUtils.addToHash(redisUtils.getDictKey(DictEnums.SYS_KEY_PAIR.getDictType()), PRIVATE_KEY, dictDto.getDictValue2());
        redisUtils.addToHash(redisUtils.getDictKey(DictEnums.SYS_KEY_PAIR.getDictType()), H5_PUBLIC_KEY, dictDto.getDictValue4());
    }
}
