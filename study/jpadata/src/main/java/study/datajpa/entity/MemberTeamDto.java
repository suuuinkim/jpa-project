package study.datajpa.entity;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberTeamDto {

    private Long id;

    private String username;

    private int age;

    private Long teamId;

    private String teamName;

    @QueryProjection // 적합한 데이터만 조회할 수 있다
    public MemberTeamDto(Long id, String username, int age, Long teamId, String teamName) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
