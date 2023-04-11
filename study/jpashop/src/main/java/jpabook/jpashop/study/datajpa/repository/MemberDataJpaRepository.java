package jpabook.jpashop.study.datajpa.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberDataJpaRepository extends JpaRepository<MemberDataJpa, Long> {
    List<MemberDataJpa> findByUsernameAndAgeGreaterThan(String username, int age);
    List<MemberDataJpa> findTop3HelloBy();
}
