package jidoly.group.controller.group;

import jidoly.group.com.CustomUser;
import jidoly.group.controller.member.dto.SignupDto;
import jidoly.group.domain.Club;
import jidoly.group.domain.FileStore;
import jidoly.group.domain.UploadFile;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final ClubService clubService;
    private final ClubRepository clubRepository;
    private final LikeRepository likeRepository;
    private final FileStore fileStore;

    @GetMapping
    public String groups(Model model) {
        /**
         * 여기 슬라이스랑, 검색 추가해야함. - 마지막
         */
        List<Club> all = clubService.findAll();
        List<GroupDto> groupList = all.stream()
                .map(club -> new GroupDto(club))
                .collect(Collectors.toList());
        System.err.println(groupList);

        /**
         * top3 정렬
         */
        List<GroupDto> top3 = likeRepository.findTop3ClubsByLikes().stream()
                .map(clubService::findById)
                .map(GroupDto::new)
                .collect(Collectors.toList());
        System.err.println(top3);

        model.addAttribute("groupList", groupList);
        model.addAttribute("top3", top3);
        return "groups/groups";
    }
    @GetMapping("/createGroup")
    public String showCreateGroups(@ModelAttribute("groupDto") GroupDto groupDto) {
        return "groups/create-group";
    }
    @PostMapping("/createGroup")
    public String createGroups(@Validated @ModelAttribute("groupDto") GroupDto groupDto,
                               @AuthenticationPrincipal CustomUser sessionUser,
                               BindingResult bindingResult) throws IOException {

        if (clubRepository.existsByClubName(groupDto.getGroupName())) {
            bindingResult.rejectValue("groupName","groupNameDuple", "중복된 클럽명이 존재합니다.");
            return "groups/create-group";
        }
        if (bindingResult.hasErrors()) {
            return "groups/create-group";
        }

        UploadFile uploadFile = (groupDto.getAttachFile() != null && !groupDto.getAttachFile().isEmpty())
                ? fileStore.storeFile(groupDto.getAttachFile())
                : UploadFile.createEmptyFile();
        Club club = Club.createClub(groupDto.getGroupName(), groupDto.getInfo(), uploadFile);
        System.err.println(club);
        clubService.createClub(sessionUser.getId(),club);

        return "redirect:/groups";
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
