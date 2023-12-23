package jidoly.group.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jidoly.group.domain.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static jidoly.group.domain.QBoard.*;
import static jidoly.group.domain.QClub.*;
import static jidoly.group.domain.QLike.*;
import static jidoly.group.domain.QMember.*;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardDto> findBoardQueryDSL() {
        List<Tuple> result = queryFactory
                .select(board,
                        JPAExpressions
                                .select(like.count().as("likeCount"))
                                .from(like)
                                .where(like.board.id.eq(board.id))
                )
                .from(board)
                .join(board.member, member).fetchJoin()
                .join(board.club, club).fetchJoin()
                .fetch();

        List<BoardDto> collect = result.stream()
                .map(tuple -> {
                    Board board = tuple.get(0, Board.class);
                    Long likeCount = tuple.get(1, Long.class);

                    return new BoardDto(
                            board.getId(),
                            board.getTitle(),
                            board.getMember().getNickname(),
                            board.getContent(),
                            Math.toIntExact(likeCount),
                            board.getUploadFiles(),
                            board.getComments()
                    );
                })
                .collect(Collectors.toList());

        return collect;
    }
}
