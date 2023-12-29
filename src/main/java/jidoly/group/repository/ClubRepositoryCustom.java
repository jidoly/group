package jidoly.group.repository;

import jidoly.group.controller.group.LikeGroupsDto;
import jidoly.group.controller.group.SearchCondition;
import jidoly.group.controller.group.SearchGroupDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ClubRepositoryCustom {
    List<LikeGroupsDto> findLikeGroups(Long memberId);

    /* 페이징되고 list -> slice 로수정*/
    Slice<SearchGroupDto> searchSlice(SearchCondition condition, Pageable pageable);

}
