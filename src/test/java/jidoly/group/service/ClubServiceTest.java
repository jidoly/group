package jidoly.group.service;

import jidoly.group.domain.Club;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClubServiceTest {

    @Autowired ClubService clubService;
    @Autowired ClubRepository clubRepository;


    @Test
    void createClub() throws Exception {
        //given
        Club club = clubService.createClub("킹", "안녕하세요");

        //when
        clubService.changeClubNameOrInfo(1L, "king", "hihi");

        //then
        Club club1 = clubRepository.findById(1L).get();
        assertThat(club1.getClubName()).isEqualTo("king");

    }

}