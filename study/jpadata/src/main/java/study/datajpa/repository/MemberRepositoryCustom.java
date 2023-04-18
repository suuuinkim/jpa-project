package study.datajpa.repository;

import study.datajpa.entity.MemberSearchCondition;
import study.datajpa.entity.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
