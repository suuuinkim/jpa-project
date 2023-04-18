package study.datajpa.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import study.datajpa.entity.MemberSearchCondition;
import study.datajpa.entity.MemberTeamDto;
import study.datajpa.entity.QMemberTeamDto;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.datajpa.entity.QMember.member;
import static study.datajpa.entity.QTeam.team;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
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
