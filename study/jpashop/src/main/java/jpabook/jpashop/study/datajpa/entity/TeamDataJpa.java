package jpabook.jpashop.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of ={"id", "name"})
public class TeamDataJpa {
    @Id @GeneratedValue
    @Column(name="team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<MemberDataJpa> members = new ArrayList<>();

    public TeamDataJpa(String name) {
        this.name = name;
    }
}
