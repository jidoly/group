package jidoly.group.repository;

import jidoly.group.controller.board.BoardDto;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardDto> findBoardQueryDSL();
}
