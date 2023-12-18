package jidoly.group.controller.member;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
    private String nickname;
    private String uploadFileName;
    private String storeFileName;

}
