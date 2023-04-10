package jpabook.jpashop.study.datajpa.repository;

import jpabook.jpashop.study.datajpa.entity.TeamDataJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {
    @PersistenceContext // JPA manage를 injection 해주는 어노테이션
    private EntityManager em;

    public TeamDataJpa save(TeamDataJpa team){
        em.persist(team);
        return team;
    }

    public void delete(TeamDataJpa team){
        em.remove(team);
    }

    public List<TeamDataJpa> findAll(TeamDataJpa team){
        return em.createQuery("select t from TeamDataJpa t", TeamDataJpa.class)
                .getResultList();
    }

    public Optional<TeamDataJpa> findById(Long id){
        TeamDataJpa team = em.find(TeamDataJpa.class, id);
        return Optional.ofNullable(team);
    }

    public long count(Long id){
        return em.createQuery("select count(t) from TeamDataJpa t", Long.class)
                .getSingleResult();
    }

}
