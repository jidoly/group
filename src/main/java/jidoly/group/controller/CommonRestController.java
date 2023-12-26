package jidoly.group.controller;

import jidoly.group.domain.FileStore;
import jidoly.group.sequrity.CustomUser;
import jidoly.group.service.BoardService;
import jidoly.group.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class CommonRestController {

    private final FileStore fileStore;
    private final ClubService clubService;
    private final BoardService boardService;

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

        boolean likeCheck = false;

        if (content.equals("group")) {
            // 좋아요 true, 취소 false
            if (clubService.likeClub(sessionUser.getId(), contentId)) {
                return 1;
            }
            return 2;
        }
        if (content.equals("board")) {
            boardService.likeBoard(sessionUser.getId(), contentId);
            return 1;
        }
        return 0;
    }
}
