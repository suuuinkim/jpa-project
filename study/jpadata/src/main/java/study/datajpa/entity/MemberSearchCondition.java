package study.datajpa.entity;

import lombok.Data;

@Data
public class MemberSearchCondition {
    private String username;
    private String teamname;
    private Integer ageGoe;
    private Integer ageLoe;
}
