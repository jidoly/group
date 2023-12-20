package jidoly.group.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

//    @GetMapping("/main")
//    public User main(@AuthenticationPrincipal User user)
//    {
//        return user;
//    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
    @GetMapping("/main2")
    public String main2() {
        return "main2";
    }
    @GetMapping("/main3")
    public String main3() {
        return "main3";
    }
    @GetMapping("/main4")
    public String main4() {
        return "main4";
    }
    @GetMapping("/main5")
    public String main5() {
        return "main5";
    }
    @GetMapping("/temp")
    public String temp() {
        return "makemain";
    }
}
