package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded // 어디에 내장되어있다는 걸 알려주기 위한 어노테이션
    private Address address;

    @OneToMany(mappedBy = "member") // order 테이블에 있는 member 필드에 대해서 매핑이 된거다
    private List<Order> orders = new ArrayList<>(); // 연관관계의 주인이 아니다! "거울"이다




}
