package jidoly.group.controller.group;

import jidoly.group.controller.board.BoardDto;
import jidoly.group.controller.board.BoardWriteDto;
import jidoly.group.domain.*;
import jidoly.group.repository.JoinRepository;
import jidoly.group.sequrity.CustomUser;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.LikeRepository;
import jidoly.group.service.BoardService;
import jidoly.group.service.ClubService;
import jidoly.group.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final JoinRepository joinRepository;
    private final JoinService joinService;
    private final BoardService boardService;

    @GetMapping
    public String groups(Model model) {
        /**
         * 여기 슬라이스랑, 검색 추가해야함. - 마지막
         */
        List<Club> all = clubService.findAll();
        List<GroupDto> groupList = all.stream()
                .map(club -> new GroupDto(club))
                .collect(Collectors.toList());

        /* top 3 */
        List<GroupDto> top3 = likeRepository.findTop3ClubsByLikes().stream()
                .map(clubService::findById)
                .map(GroupDto::new)
                .collect(Collectors.toList());

        model.addAttribute("groupList", groupList);
        model.addAttribute("top3", top3);
        return "groups/groups";
    }

    /**
     * 그룹 생성 페이지
     */
    @GetMapping("/createGroup")
    public String showCreateGroups(@ModelAttribute("groupDto") GroupDto groupDto) {
        return "groups/create-group";
    }
    @PostMapping("/createGroup")
    public String createGroups(@Validated @ModelAttribute("groupDto") GroupDto groupDto,
                               @AuthenticationPrincipal CustomUser sessionUser,
                               BindingResult bindingResult) throws IOException {

        if (clubRepository.existsByClubName(groupDto.getGroupName())) {
            bindingResult.rejectValue("groupName","duplicate");
            return "groups/create-group";
        }
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "groups/create-group";
        }

        UploadFile uploadFile = (groupDto.getAttachFile() != null && !groupDto.getAttachFile().isEmpty())
                ? fileStore.storeFile(groupDto.getAttachFile())
                : null;
        Club club = Club.createClub(groupDto.getGroupName(), groupDto.getInfo(), uploadFile);

        clubService.createClub(sessionUser.getId(),club);

        return "redirect:/groups";
    }

    /**
     * 그룹상세 페이지
     */
    @GetMapping("/group")
    public String group(@RequestParam(name = "groupId") Long groupId,
                        @AuthenticationPrincipal CustomUser sessionUser,
                        Model model) {

        /* 그룹 정보 */
        GroupDto group = new GroupDto(clubService.findById(groupId));
        model.addAttribute("group", group);

        /* 가입 정보 - 관리자, 가입여부용 */
        String joinStatus = getJoinStatus(groupId, sessionUser);
        model.addAttribute("joinStatus", joinStatus);
        model.addAttribute("memberId", sessionUser.getId());

        /* 좋아요 여부 */
        boolean isLikeExist = likeRepository.findByMemberIdAndClubId(sessionUser.getId(), groupId)
                .isPresent();
        model.addAttribute("isLikeExist", isLikeExist);

        /* 게시판 정보 */
        List<BoardDto> all = boardService.findAllByGroupId(groupId);
        Optional<BoardDto> lastNotice = all.stream()
                .filter(board -> board.getBoardCategory() == BoardCategory.NOTICE)
                .findFirst();

        model.addAttribute("all", all);
        model.addAttribute("lastNotice", lastNotice.orElse(null));

        return "groups/group";
    }



    /**
     * 그룹 가입 / 취소 페이지
     */
    @PostMapping("group/joinGroup")
    public String joinGroup(@RequestParam("groupId") Long groupId,
                            @RequestParam("memberId") Long memberId,
                            RedirectAttributes redirectAttributes,Model model) {

        /* repository 에서 조회 -> 존재 X 신청, 존재 O 취소 */
        joinRepository.findByMemberIdAndClubId(memberId, groupId)
                .ifPresentOrElse(
                        join -> joinService.denyJoin(join.getId()),
                        () -> joinService.applyJoin(memberId, groupId)
                );

        redirectAttributes.addAttribute("groupId", groupId);

        return "redirect:/groups/group";
    }

    @GetMapping("/group/write")
    public String groupWrite(@ModelAttribute("writeDto") BoardWriteDto writeDto,
                             @AuthenticationPrincipal CustomUser sessionUser,
                             @RequestParam(name = "groupId") Long groupId,
                             Model model) {

        BoardCategory[] categories = writeDto.getCategory().values();
        model.addAttribute("categories", categories);
        model.addAttribute("groupId", groupId);

        /* 공지사항 작성가능 여부 check */
        String joinStatus = getJoinStatus(groupId, sessionUser);
        model.addAttribute("joinStatus", joinStatus);

        return "groups/group-write";
    }
    @PostMapping("/group/write")
    public String postGroupWrite(@Validated @ModelAttribute("writeDto") BoardWriteDto writeDto,
                             RedirectAttributes redirectAttributes,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUser sessionUser,
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            // 유효성 검증 오류 처리, 예를 들어 오류 메시지와 함께 폼 페이지로 돌아가기
            return "groups/group-write";
        }

        writeDto.setMemberId(sessionUser.getId());
        if (writeDto.getCategory() == null) {
            writeDto.setCategory(BoardCategory.BOARD);
        }
        Long aLong = boardService.writePost(writeDto);
        log.debug("글 등록완료 = {}", aLong);
        redirectAttributes.addAttribute("groupId", writeDto.getGroupId());


        return "redirect:/groups/group";
    }

    @GetMapping("/group/board")
    public String groupBoard(
            @RequestParam(name = "boardId") Long boardId,
            @AuthenticationPrincipal CustomUser sessionUser,
            Model model) {

        /* 게시글 상세 */
        BoardDto findBoard = boardService.findBoardById(boardId);
        model.addAttribute("board", findBoard);

        /* 좋아요 여부 */
        boolean isLikeExist = likeRepository.findByMemberIdAndBoardId(sessionUser.getId(), boardId)
                .isPresent();
        System.err.println(isLikeExist);
        model.addAttribute("isLikeExist", isLikeExist);


        return "groups/group-board";
    }

    @GetMapping("/group/member")
    public String groupMember(@RequestParam("groupId") Long groupId,
                              Model model) {

        List<Join> findJoins = joinRepository.findByClubId(groupId);

        List<JoinMemberDto> joinList = findJoins.stream()
                .map(join -> new JoinMemberDto(join))
                .collect(Collectors.toList());


        Map<JoinStatus, List<JoinMemberDto>> groupedByStatus = joinList.stream()
                .collect(Collectors.groupingBy(JoinMemberDto::getStatus));

        // 모든 상태에 대한 리스트 생성
        List<JoinMemberDto> manages = groupedByStatus.getOrDefault(JoinStatus.MANAGED, List.of());
        List<JoinMemberDto> waits = groupedByStatus.getOrDefault(JoinStatus.WAIT, List.of());
        List<JoinMemberDto> joins = groupedByStatus.getOrDefault(JoinStatus.JOINED, List.of());

        int memberCount = manages.size() + joins.size();

        model.addAttribute("groupId", groupId);
        model.addAttribute("manages", manages);
        model.addAttribute("waits", waits);
        model.addAttribute("joins", joins);
        model.addAttribute("memberCount", memberCount);

        return "groups/group-member";
    }

    @GetMapping("/group/member/applyJoin")
    public String applyJoin(@RequestParam("joinId") Long joinId,
                            @RequestParam("groupId") Long groupId,
                            RedirectAttributes redirectAttributes) {

        joinService.acceptJoin(joinId);

        redirectAttributes.addAttribute("groupId", groupId);

        return "redirect:/groups/group/member";
    }
    @GetMapping("/group/member/cancelJoin")
    public String cancelJoin(@RequestParam("joinId") Long joinId,
                            @RequestParam("groupId") Long groupId,
                            RedirectAttributes redirectAttributes) {

        joinService.denyJoin(joinId);
        redirectAttributes.addAttribute("groupId", groupId);

        return "redirect:/groups/group/member";
    }
    @PostMapping("/group/member/setManager")
    public ResponseEntity<String> setManager(@RequestParam("joinId") Long joinId) {

        joinService.setManagerJoin(joinId);

        return ResponseEntity.ok("OK");

    }

    private String getJoinStatus(Long groupId, CustomUser sessionUser) {
        String joinStatus = joinRepository.findByMemberIdAndClubId(sessionUser.getId(), groupId)
                .map(join -> join.getStatus() != null ? join.getStatus().toString() : "no")
                .orElse("no");
        return joinStatus;
    }

}
