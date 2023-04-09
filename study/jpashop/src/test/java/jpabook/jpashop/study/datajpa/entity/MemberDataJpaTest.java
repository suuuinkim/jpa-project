package jpabook.jpashop.study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)

class MemberDataJpaTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() throws Exception{
        //given
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

        // 초기화
        em.flush(); // 강제로 db insert 쿼리를 날려버림
        em.clear(); // 영속성 컨테이너에 있는 캐시를 다 날려버림

        // 확인
        List<MemberDataJpa> members = em.createQuery("select m from MemberDataJpa m", MemberDataJpa.class).getResultList();

        for (MemberDataJpa member : members) {
            System.out.println("member = " + member);
            System.out.println("->member.team = " + member);

        }

    }
}