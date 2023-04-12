package jpabook.jpashop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import jpabook.jpashop.study.datajpa.entity.MemberSearchCondition;
import jpabook.jpashop.study.datajpa.entity.MemberTeamDto;
import jpabook.jpashop.study.datajpa.entity.TeamDataJpa;
import jpabook.jpashop.study.datajpa.repository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
//@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() throws Exception{
        //given
        MemberDataJpa member = new MemberDataJpa("memberA");
        //when
        MemberDataJpa savedMember = memberJpaRepository.save(member);
        MemberDataJpa findMember = memberJpaRepository.findById(savedMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception{
        MemberDataJpa m1 = new MemberDataJpa("AAA", 10);
        MemberDataJpa m2 = new MemberDataJpa("AAA", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<MemberDataJpa> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void searchTest() throws Exception{
        TeamDataJpa teamA = new TeamDataJpa("teamA");
        TeamDataJpa teamB = new TeamDataJpa("teamB");
        em.persist(teamA);
        em.persist(teamB);

        MemberDataJpa member1 = new MemberDataJpa("member1", 10, teamA);
        MemberDataJpa member2 = new MemberDataJpa("member2", 20, teamA);
        MemberDataJpa member3 = new MemberDataJpa("member3", 30, teamB);
        MemberDataJpa member4 = new MemberDataJpa("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();
//        condition.setAgeGoe(35);
//        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);

        assertThat(result).extracting("username").containsExactly("member3", "member4");

    }


}
