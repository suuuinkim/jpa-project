package study.datajpa.repository;

import study.datajpa.entity.Member;
import study.datajpa.entity.MemberSearchCondition;
import study.datajpa.entity.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * 항상 cumtom이 필요한 건 아니다
     * 
     */
    List<Member> findMemberCustom();
//    List<MemberTeamDto> search(MemberSearchCondition condition);
}
