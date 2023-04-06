package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA 자체를 잘 이해하는 것이 가장 중요하다
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * select m from Member m where m.name = :name
     */
    List<Member> findByName(String name);
}
