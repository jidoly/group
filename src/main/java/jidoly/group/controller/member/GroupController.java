package jidoly.group.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    @GetMapping
    public String groups() {
        return "groups/groups";
    }

    @GetMapping("/group")
    public String group(@RequestParam(name = "groupId") Long groupId) {
        return "groups/group";
    }

    @GetMapping("/group/write")
    public String groupWrite(@RequestParam(name = "groupId") Long id) {
        return "groups/group-write";
    }

    @GetMapping("/group/board")
    public String groupBoard(
            @RequestParam(name = "groupId") Long groupId,
            @RequestParam(name = "boardId") Long boardId) {
        return "groups/group-board";
    }
}
