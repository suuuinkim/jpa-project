package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용이다! db에 읽기전용이니까 리소스 너무 많이 쓰지말라고 알려주는 것
@RequiredArgsConstructor // @Autowired 없이 final 과 함께 사용할 수 있음
public class MemberService {

   // @Autowired
   // final을 해놓으면 컴파일 시점에 체크가 가능함!
    private final MemberRepository memberRepository;


    /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional // 쓰기에는 readonly 넣으면 데이터 변경이 안됨
    public Long join(Member member){

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 같은 이름 찾기
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 회원 한 명 조회
    public Member findOnd(Long seq){
        return memberRepository.findOne(seq);
    }

}
