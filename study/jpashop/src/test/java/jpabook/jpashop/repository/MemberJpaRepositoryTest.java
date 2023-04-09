package jpabook.jpashop.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import jpabook.jpashop.study.datajpa.repository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    
    @Test
    public void testMember() throws Exception{
        //given
        MemberDataJpa member = new MemberDataJpa("memberA");
        //when
        MemberDataJpa savedMember = memberJpaRepository.save(member);
        MemberDataJpa findMember = memberJpaRepository.find(savedMember.getId());
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);
    }
}