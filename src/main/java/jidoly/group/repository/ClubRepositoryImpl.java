package jidoly.group.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jidoly.group.controller.group.LikeGroupsDto;
import jidoly.group.controller.group.QLikeGroupsDto;
import jidoly.group.domain.QClub;
import jidoly.group.domain.QJoin;
import jidoly.group.domain.QLike;
import jidoly.group.domain.QUploadFile;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static jidoly.group.domain.QClub.*;
import static jidoly.group.domain.QJoin.*;
import static jidoly.group.domain.QLike.*;
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
}
