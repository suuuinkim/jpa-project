package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    
    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @Transactional // test 에 있으면 test가 끝나고 db를 롤백함 - 반복적인 테스트를 위해
    @Rollback(false) // 해당 어노테이션 넣으면 rollback 안하고 커밋함
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUserName("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        Assertions.assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member" + (findMember == member)); // true 영속성 컨텍스트에서 아이디값이 같으면 같은 엔티티로 봄
    }
}
