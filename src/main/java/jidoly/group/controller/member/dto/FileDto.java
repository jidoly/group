package jidoly.group.controller.member.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDto {

    private MultipartFile attachFile;
    private String fileName;

}
