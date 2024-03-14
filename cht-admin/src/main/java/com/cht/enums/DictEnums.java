package com.cht.enums;


public enum DictEnums {
    SYS_KEY_PAIR("SYS_KEY_PAIR", "0"),
    ;

    private String dictType;

    private String dictValue;

    DictEnums(String dictType, String dictValue) {
        this.dictType = dictType;
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
}
