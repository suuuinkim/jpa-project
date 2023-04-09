package jpabook.jpashop.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class MemberDataJpa {

    @Id
    @GeneratedValue
    @Column(name ="member_id")
    private Long id;
    private String userName;
    private int age;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="team_id")
    private TeamDataJpa team;

    /**
     * JPA는 기본적으로 default 생성자가 있어야된다
     * private으로는 만들면 안됨! 왜?
     * 프록시 기술을 사용할때 private으로 막아두면 생성이 안될 수 있음
     */
//    protected MemberDataJpa() {}

    public MemberDataJpa(String userName) {
        this.userName = userName;
    }

    public MemberDataJpa(String userName, int age, TeamDataJpa team) {
        this.userName = userName;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }
    }

    /**
     * 이름 바꾸는게 필요하다면?
     * 메소드를 제공하는 것이 더 좋은 방법이다
     */
    public void changeUsername(String userName){
        this.userName = userName;
    }

    // ==== 연관관계를 세팅하는 메소드 ==== //
    public void changeTeam(TeamDataJpa team){
        this.team = team;
        team.getMembers().add(this);
    }
}
