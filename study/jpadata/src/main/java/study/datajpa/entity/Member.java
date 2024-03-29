package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED) // 기본 생성자를 작성하지 않아도 기본 생성자를 생성할 수 있으며, 접근제어자를 protected로 설정
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)

@NamedEntityGraph(
        name = "Member.all", attributeNodes = @NamedAttributeNode("team")
)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String username) {
        this(username, 0);
    }


    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if(team != null) changeTeam(team);
    }

    public void changeUsername(String username){
        this.username = username;
    }
    public void changeTeam(Team team){
        this.team = team;

    }
}
