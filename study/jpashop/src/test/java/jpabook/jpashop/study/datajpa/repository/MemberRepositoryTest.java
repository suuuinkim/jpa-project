package jpabook.jpashop.study.datajpa.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberDataJpaRepository memberRepository;

    @Test
    public void testMember() throws Exception{
        //given
        MemberDataJpa member = new MemberDataJpa("memberA");
        //when
        MemberDataJpa savedMember = memberRepository.save(member);
        MemberDataJpa findMember = memberRepository.findById(savedMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);
    }
}