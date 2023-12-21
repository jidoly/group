package jidoly.group.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GroupController {

    @GetMapping("/groups")
    public String groups() {
        return "groups/groups";
    }


}
