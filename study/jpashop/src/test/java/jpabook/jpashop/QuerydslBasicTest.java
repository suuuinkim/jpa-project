package jpabook.jpashop;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import jpabook.jpashop.study.datajpa.entity.QMemberDataJpa;
import jpabook.jpashop.study.datajpa.entity.TeamDataJpa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static jpabook.jpashop.study.datajpa.entity.QMemberDataJpa.memberDataJpa;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
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
    }

    @Test
    public void startJPQL() throws Exception{
        //member1
        String qlString = "select m from MemberDataJpa m " +
                "where m.username = :username";

        MemberDataJpa findMember = em.createQuery(qlString, MemberDataJpa.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    /**
     * 1) 컴파일 시점에 오류를 확인할 수 있다
     * 2) 파라미터 바인딩을 자동으로 해결해준다
     */
    @Test
    public void startQuerydsl() throws Exception{

//        QMemberDataJpa m = new QMemberDataJpa("m");
//        QMemberDataJpa m = QMemberDataJpa.memberDataJpa;

        MemberDataJpa findMember = queryFactory
                .select(memberDataJpa)
                .from(memberDataJpa)
                .where(memberDataJpa.username.eq("member1")) // 파라미터 바인딩 처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() throws Exception{
        MemberDataJpa findMember = queryFactory
                .selectFrom(memberDataJpa)
                .where(memberDataJpa.username.eq("member1")
                        .and(memberDataJpa.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void searchAndParam() throws Exception{
        MemberDataJpa findMember = queryFactory
                .selectFrom(memberDataJpa)
                .where(
                        memberDataJpa.username.eq("member1"),
                        memberDataJpa.age.eq(10)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }
}
