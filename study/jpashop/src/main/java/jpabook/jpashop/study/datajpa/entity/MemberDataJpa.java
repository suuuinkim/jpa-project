package jpabook.jpashop.study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class MemberDataJpa {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;

    /**
     * JPA는 기본적으로 default 생성자가 있어야된다
     * private으로는 만들면 안됨! 왜?
     * 프록시 기술을 사용할때 private으로 막아두면 생성이 안될 수 있음
     */
    protected MemberDataJpa() {
    }

    public MemberDataJpa(String userName) {
        this.userName = userName;
    }
    /**
     * 이름 바꾸는게 필요하다면?
     * 메소드를 제공하는 것이 더 좋은 방법이다
     */
    public void changeUsername(String userName){
        this.userName = userName;
    }
}
