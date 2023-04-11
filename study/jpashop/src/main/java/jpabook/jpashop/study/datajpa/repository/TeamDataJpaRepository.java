package jpabook.jpashop.study.datajpa.repository;

import jpabook.jpashop.study.datajpa.entity.TeamDataJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDataJpaRepository extends JpaRepository<TeamDataJpa, Long> {
}
