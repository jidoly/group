package jidoly.group.service;

import jidoly.group.domain.Club;
import jidoly.group.domain.Member;
import jidoly.group.repository.ClubRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        Optional<Club> result = clubRepository.findByClubName("킹");
        System.out.println("result = " + result);
//        clubService.changeClubNameOrInfo(0L, "king", "hihi");
        //when

        //then

    }

}