package com.cht.admin.pojo.system;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RolMenuVo implements Serializable {

    public RolMenuVo(Long value, String label) {
        this.label = label;
        this.value = value;
    }

    @Serial
    private static final long serialVersionUID = -8231668535285842452L;
    private Long value;
    private String label;
    private List<RolMenuVo> children;
}
