package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void JpaEventBaseEntity() throws Exception{
        //given
        Member member = new Member("member");
        memberRepository.save(member); // @PrePersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        //then
        System.out.println("findMember = " + findMember.getCreatedDate());
        System.out.println("findMember = " + findMember.getLastModifiedDate());
        System.out.println("findMember = " + findMember.getCreatedBy());
        System.out.println("findMember = " + findMember.getLastModifiedBy());

    }
}