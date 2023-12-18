package jidoly.group.controller.member;

import jidoly.group.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    @ResponseBody
    @GetMapping("/main")
    public User main(@AuthenticationPrincipal User user)
    {
        return user;
    }
}
