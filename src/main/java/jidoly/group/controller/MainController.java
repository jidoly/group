package jidoly.group.controller;

import jidoly.group.controller.group.GroupDto;
import jidoly.group.controller.group.LikeGroupsDto;
import jidoly.group.controller.member.dto.MemberDto;
import jidoly.group.controller.member.dto.MyGroupDto;
import jidoly.group.domain.Join;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.sequrity.CustomUser;
import jidoly.group.service.ClubService;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final ClubService clubService;
    private final ClubRepository clubRepository;

    @GetMapping("/main")
    public String main(Model model,
                       @AuthenticationPrincipal CustomUser sessionUser) {


        /* 유저 정보 */
        String username = sessionUser.getUsername();
        Member findMember = memberService.findMemberByUsername(username);
        MemberDto member = new MemberDto(findMember);

        /* top 3 */
        List<GroupDto> top3 = likeRepository.findTop3ClubsByLikes().stream()
                .map(clubService::findById)
                .map(GroupDto::new)
                .collect(Collectors.toList());

        List<LikeGroupsDto> likeGroups = clubRepository.findLikeGroups(sessionUser.getId());

        model.addAttribute("member", member);
        model.addAttribute("top3", top3);
        model.addAttribute("likeGroups", likeGroups);

        return "main";
    }

}
