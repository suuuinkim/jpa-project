package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // @Component 어노테이션이 있기 때문에 자동으로 컴포넌트 스캔의 대상이 됨
@Transactional(readOnly = true) // readOnly = true : 조회는 성능 최적화
@RequiredArgsConstructor
public class MemberService {

    // 변경할 일이 없기 때문에 final
    private final MemberRepository memberRepository;

//    생성자 인젝션 (@AllArgsConstructor)
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


    /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member){
        // 유효성 검사 : 중복회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMEmbers = memberRepository.findByName(member.getName());
        if(!findMEmbers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }


    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name); // 변경감지에 의한 update
    }
}
