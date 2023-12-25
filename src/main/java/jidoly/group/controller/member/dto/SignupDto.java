package jidoly.group.controller.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jidoly.group.domain.Member;
import jidoly.group.domain.UploadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    private String pastPw;

    private MultipartFile attachFile;

    private String storeFileName;

}
