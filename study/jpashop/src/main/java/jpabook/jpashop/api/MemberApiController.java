package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     * 엔티티를 직접 반환 하면 안됨
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }


    /**
     * api 스펙에 맞는 DTO를 만들자
     * 한번 감싸서 반환하기 때문에 유연성이 생긴다
     */
    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }
    /**
     * api를 만들때는 엔티티를 파라미터로 받지 말자
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 별도의 엔티티를 사용하면 api 스펙이 바뀌지 않는다는 장점이 있다
     * 절대로 엔티티를 외부에 노출하거나 파라미터에 그대로 받는 것은 절대로 하면 안됨
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName()); // update가 완료되고 트랜잭션이 끝남
        Member findMember = memberService.findOne(id); // id를 가지고 쿼리를 해서 변경 실행
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }


    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }
    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;

    }
    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}

