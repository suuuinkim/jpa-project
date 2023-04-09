package jpabook.jpashop.study.datajpa.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberJpaRepository {
    @PersistenceContext // 스프링컨테이너가 JPA에 있는 영속성 컨테이너에 EntityManager를 가져다줌
    private EntityManager em;

    public MemberDataJpa save(MemberDataJpa member){
        em.persist(member);
        return member;
    }

    public MemberDataJpa find(Long id){
        return em.find(MemberDataJpa.class, id);
    }


}
