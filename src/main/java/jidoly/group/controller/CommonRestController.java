package jidoly.group.controller;

import jidoly.group.controller.board.CommentDto;
import jidoly.group.controller.group.SearchCondition;
import jidoly.group.controller.group.SearchGroupDto;
import jidoly.group.domain.FileStore;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import jidoly.group.repository.MemberRepository;
import jidoly.group.sequrity.CustomUser;
import jidoly.group.service.BoardService;
import jidoly.group.service.ClubService;
import jidoly.group.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class CommonRestController {

    private final FileStore fileStore;
    private final ClubService clubService;
    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ClubRepository clubRepository;

    @GetMapping("/upload/{filename}")
    public Resource downloadImaage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    /**
     * return 0 : 로직 실패
     * return 1 : 좋아요 성공
     * return 2 : 좋아요 삭제 성공
     */
    @PostMapping("/rest/like")
    public int like(@AuthenticationPrincipal CustomUser sessionUser,
                    @RequestParam("contentId") Long contentId,
                    @RequestParam("content") String content
    ) {
        if (content.equals("group")) {
            // 좋아요 true, 취소 false
            if (clubService.likeClub(sessionUser.getId(), contentId)) {
                return 1;
            }
            return 2;
        }
        if (content.equals("board")) {
            if (boardService.likeBoard(sessionUser.getId(), contentId)) {
                return 1;
            }
            return 2;
        }
        return 0;
    }

    @PostMapping("/rest/comment")
    public String addComment(@RequestBody CommentDto commentDto,
                              @AuthenticationPrincipal CustomUser sessionUser) {

        System.err.println(commentDto);
        memberRepository.findById(sessionUser.getId());
        String nickname = memberService.findMemberByUsername(sessionUser.getUsername()).getNickname();
        commentDto.setWriter(nickname);
        boardService.addCommentToBoard(commentDto);

        return nickname;
    }

    @GetMapping("/rest/search")
    public Slice<SearchGroupDto> restSearch(SearchCondition condition, Pageable pageable) {

        return clubRepository.searchSlice(condition, pageable);
    }

}
