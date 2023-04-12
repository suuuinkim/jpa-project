package jpabook.jpashop.study.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import jpabook.jpashop.study.datajpa.entity.MemberSearchCondition;
import jpabook.jpashop.study.datajpa.entity.MemberTeamDto;
import jpabook.jpashop.study.datajpa.entity.QMemberTeamDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import static jpabook.jpashop.study.datajpa.entity.QMemberDataJpa.memberDataJpa;
import static jpabook.jpashop.study.datajpa.entity.QTeamDataJpa.teamDataJpa;
import static org.springframework.util.StringUtils.*;

/**
 * 왜 수정기능은 없을까?
 * JPA는 기본적으로 변경감지 기능을 사용해서 다 변경을 함
 * 그래서 update라는 메소드가 필요없음
 * 자바 컬렉션과 동일!
 */
@Repository
public class MemberJpaRepository {
    //@PersistenceContext // 스프링컨테이너가 JPA에 있는 영속성 컨테이너에 EntityManager를 가져다줌
    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 저장
     */
    public MemberDataJpa save(MemberDataJpa member){
        em.persist(member);
        return member;
    }

    /**
     * 삭세
     */
    public void delete(MemberDataJpa member){
        em.remove(member);
    }

    /**
     * 목록조회
     */
    public List<MemberDataJpa> findAll(){
        return em.createQuery("select m from MemberDataJpa m", MemberDataJpa.class)
                .getResultList();
    }

    /**
     * Optional 사용해서 null인지 아닌지 체크
     */
    public Optional<MemberDataJpa> findById(Long id){
        MemberDataJpa member = em.find(MemberDataJpa.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("select count(m) from MemberDataJpa m", Long.class)
                .getSingleResult();
    }

    /**
     * 단건 조회
     */
    public MemberDataJpa find(Long id){
        return em.find(MemberDataJpa.class, id);
    }

    /**
     * 회원의 나이 기준으로 조회
     */
    public List<MemberDataJpa> findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from MemberDataJpa m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) {
            builder.and(memberDataJpa.username.eq(condition.getUsername()));
        }

        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(teamDataJpa.name.eq(condition.getTeamName()));
        }

        if (condition.getAgeGoe() != null) {
            builder.and(memberDataJpa.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            builder.and(memberDataJpa.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        memberDataJpa.id.as("memberId"),
                        memberDataJpa.username,
                        memberDataJpa.age,
                        teamDataJpa.id.as("teamId"),
                        teamDataJpa.name.as("teamName")))
                .from(memberDataJpa)
                .leftJoin(memberDataJpa.team, teamDataJpa)
                .where(builder)
                .fetch();

    }
}
