package jidoly.group.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jidoly.group.controller.group.*;
import jidoly.group.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import java.util.List;

import static jidoly.group.domain.QClub.*;
import static jidoly.group.domain.QJoin.*;
import static jidoly.group.domain.QLike.like;
import static jidoly.group.domain.QUploadFile.*;

@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LikeGroupsDto> findLikeGroups(Long memberId) {
        return queryFactory
                .selectDistinct(new QLikeGroupsDto(
                        club.id,
                        club.clubName,
                        club.info,
                        join.status,
                        new CaseBuilder()
                                .when(like.club.id.isNotNull().and(like.member.id.eq(memberId)))
                                .then("Liked")
                                .otherwise("Not Liked").as("like_status"),
                        uploadFile.storeFileName
                ))
                .from(club)
                .leftJoin(club.joins, join)
                .leftJoin(club.likes, like)
                .on(like.member.id.eq(memberId))
                .leftJoin(club.uploadFiles, uploadFile)
                .where(join.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public Slice<SearchGroupDto> searchSlice(SearchCondition condition, Pageable pageable) {


        List<SearchGroupDto> content = getContent(condition, pageable);
        Long total = getTotal(condition);

        return new PageImpl<>(content, pageable, total);
    }

    private List<SearchGroupDto> getContent(SearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(new QSearchGroupDto(
                        club.id,
                        club.clubName,
                        club.info,
                        uploadFile.storeFileName,
                        Expressions.asNumber(like.countDistinct()).coalesce(0L).as("likeCount"),
                        Expressions.asNumber(join.countDistinct()).coalesce(0L).as("memberCount")
                ))
                .from(club)
                .leftJoin(uploadFile)
                .on(uploadFile.club.id.eq(club.id)
                        .and(uploadFile.id.eq(
                                JPAExpressions.select(uploadFile.id.max()) //쿼리 결과 뻥튀기 방지 가장 최근 파일하나만 조인
                                        .from(uploadFile)
                                        .where(uploadFile.club.id.eq(club.id))
                        ))
                )
                .leftJoin(like)
                .on(club.id.eq(like.club.id))
                .leftJoin(join)
                .on(club.id.eq(join.club.id).and(join.status.ne(JoinStatus.WAIT)))
                .groupBy(
                        club.id,
                        club.clubName,
                        club.info,
                        uploadFile.storeFileName
                )
                .where(
                        groupNameEq(condition.getGroupName()),
                        infoEq(condition.getInfo())
                )
                .orderBy(
                        orderEq(condition.getOrderCondition())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();
    }

    private Long getTotal(SearchCondition condition) {
        Long total = queryFactory
                .select(club.countDistinct())
                .from(club)
                .where(
                        groupNameEq(condition.getGroupName()),
                        infoEq(condition.getInfo())
                )
                .fetchOne();
        return total;
    }

    private BooleanExpression groupNameEq(String groupName) {
        return StringUtils.hasText(groupName) ? club.clubName.like("%"+groupName +"%") : null;
    }
    private BooleanExpression infoEq(String info) {
        return StringUtils.hasText(info) ? club.info.like("%" +info +"%") : null;
    }

    private OrderSpecifier<Long> orderEq(String getOrderCondition) {

        if (!StringUtils.hasText(getOrderCondition)) {
            return club.id.desc();
        }
        if (getOrderCondition.equals("like")) {
            return like.countDistinct().desc();
        } else {
            return join.countDistinct().desc();
        }
    }


}
