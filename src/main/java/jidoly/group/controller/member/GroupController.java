package jidoly.group.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    @GetMapping
    public String groups() {
        return "groups/groups";
    }

    @GetMapping("/{id}")
    public String group(@PathVariable Long id) {
        return "groups/group";
    }

    @GetMapping("/write")
    public String wirte() {
        return "groups/group-write";
    }
}
