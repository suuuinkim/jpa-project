package jpabook.jpashop.study.datajpa.entity;

import lombok.Data;

@Data
public class MemberSearchCondition {
    // 회원명, 팀명, 나이(ageCoe, ageLoe)
    private String username;
    private String teamName;
    private Integer  ageGoe;
    private Integer ageLoe;



}
