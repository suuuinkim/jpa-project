package jpabook.jpashop.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import jpabook.jpashop.study.datajpa.repository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
//@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() throws Exception{
        //given
        MemberDataJpa member = new MemberDataJpa("memberA");
        //when
        MemberDataJpa savedMember = memberJpaRepository.save(member);
        MemberDataJpa findMember = memberJpaRepository.findById(savedMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception{
        MemberDataJpa m1 = new MemberDataJpa("AAA", 10);
        MemberDataJpa m2 = new MemberDataJpa("AAA", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<MemberDataJpa> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }
}
