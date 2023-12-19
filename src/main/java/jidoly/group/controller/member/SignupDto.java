package jidoly.group.controller.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignupDto {

    @Email
    private String username;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    @NotBlank
    private String validPw;

    private MultipartFile attachFile;

}
