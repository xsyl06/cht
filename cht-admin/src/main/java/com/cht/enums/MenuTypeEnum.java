package com.cht.enums;

public enum MenuTypeEnum {
    // 菜单类型
    MENU(0, "菜单"),
    IFRAME(1, "iframe"),
    LINK(2, "链接"),
    BUTTON(3, "按钮")
    ;


    private int type;
    private String value;


    MenuTypeEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
