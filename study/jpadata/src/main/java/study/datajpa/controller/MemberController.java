package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable){
//        page 1 부터 사용하는 방법 1
//        PageRequest request = PageRequest.of(1, 2);
//        Page<Member> page = memberRepository.findAll(request); // 이렇게 하면 엔티티를 그대로 반환하는 것이기 때문에 절대 이렇게 반환해서는 안됨

        Page<Member> page = memberRepository.findAll(pageable); // 이렇게 하면 엔티티를 그대로 반환하는 것이기 때문에 절대 이렇게 반환해서는 안됨
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }

    @PostConstruct
    public void init(){
        for(int i=0; i<100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }

}
