package jpabook.jpashop.repository;

import jpabook.jpashop.study.datajpa.entity.MemberDataJpa;
import jpabook.jpashop.study.datajpa.repository.MemberDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {
    @Autowired
    MemberDataJpaRepository memberJpaRepository;


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
    public void basicCrud() throws Exception{
        //given
        MemberDataJpa member1 = new MemberDataJpa("member1");
        MemberDataJpa member2 = new MemberDataJpa("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회 검증
        MemberDataJpa findMember1 = memberJpaRepository.findById(member1.getId()).get();
        MemberDataJpa findMember2 = memberJpaRepository.findById(member1.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<MemberDataJpa> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(count).isEqualTo(0);
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
    
    @Test
    public void findHelloBy() throws Exception{
        List<MemberDataJpa> helloBy = memberJpaRepository.findTop3HelloBy();
    }

    @Test
    public void testQuery() throws Exception{
        MemberDataJpa m1 = new MemberDataJpa("AAA", 10);
        MemberDataJpa m2 = new MemberDataJpa("AAA", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<MemberDataJpa> result = memberJpaRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

}