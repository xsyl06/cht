package com.cht.admin.pojo;

import com.cht.mp.pojo.database.UserInfoDto;
import lombok.Data;

import java.io.Serial;
import java.util.List;
@Data
public class LoginUserInfo extends UserInfoDto {
    @Serial
    private static final long serialVersionUID = 6383498116501731892L;

    private List<String> auths;

    private List<String> roles;

    private String accessToken;

    private String refreshToken;

    private Long expires;
}
