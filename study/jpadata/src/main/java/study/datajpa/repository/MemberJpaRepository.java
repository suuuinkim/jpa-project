package study.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.datajpa.entity.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static study.datajpa.entity.QMember.member;
import static study.datajpa.entity.QTeam.team;

/**
 * 왜 수정기능은 없을까?
 * JPA는 기본적으로 변경감지 기능을 사용해서 변경을 함
 * 그래서 update라는 메소드가 필요없음
 * 자바 컬렉션과 동일!!!
 */
@Repository
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 저장
     */
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    /**
     * 삭제
     */
    public void delete(Member member){
        em.remove(member);
    }

    /**
     * 목록 조회
     */
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    /**
     * Optional 사용해서 null 체크
     */
    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("select m from Member m", Long.class).getSingleResult();
    }

    /**
     * 단건 조회
     */
    public Member find(Long id){
        return em.find(Member.class, id);
    }

    /**
     * 회원의 나이 기준으로 조회
     */
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age){

        return em.createQuery("update Member m set m.age = m.age + 1 " +
                        "where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }

        if (StringUtils.hasText(condition.getTeamname())) {
            builder.and(team.name.eq(condition.getTeamname()));
        }

        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    public List<MemberTeamDto> search(MemberSearchCondition condition){
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        testNameEq(condition.getTeamname()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username)? member.username.eq(username) : null;
    }

    private BooleanExpression testNameEq(String teamName) {
        return hasText(teamName)? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }


}
