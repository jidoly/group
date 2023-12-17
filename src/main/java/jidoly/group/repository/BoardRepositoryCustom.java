package jidoly.group.repository;

import jidoly.group.domain.BoardDto;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardDto> findBoardQueryDSL();
}
