package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor // @Autowired 없이 final 과 함께 사용할 수 있음
public class MemberRepository {

    private final EntityManager em;

    // 등록
    public void save(Member member) {
        em.persist(member);
    }

    // 상세조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 목록조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();

    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
    }


}
