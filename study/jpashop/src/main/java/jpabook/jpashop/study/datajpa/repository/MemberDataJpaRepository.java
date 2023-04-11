package jpabook.jpashop.study.datajpa.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberDataJpaRepository extends JpaRepository<MemberDataJpa, Long> {
    List<MemberDataJpa> findByUsernameAndAgeGreaterThan(String username, int age);
    List<MemberDataJpa> findTop3HelloBy();

    /**
     * 오타가 생기면 에플리케이션 실행시점에 문법에 오류가 있는지에 대한걸 알 수 있다
     */
    @Query("select m from MemberDataJpa m where m.username = :username and m.age = :age")
    List<MemberDataJpa> findUser(@Param("username") String username, @Param("age") int age);
}
