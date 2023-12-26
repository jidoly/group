package jidoly.group.repository;

import jidoly.group.controller.group.LikeGroupsDto;

import java.util.List;

public interface ClubRepositoryCustom {
    List<LikeGroupsDto> findLikeGroups(Long memberId);
}
