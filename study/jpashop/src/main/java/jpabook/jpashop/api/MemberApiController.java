package jpabook.jpashop.api;

import jpabook.jpashop.controller.MemberForm;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController // controller + responsebody
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 엔티티를 위한 dto를 만들어야됨 왜냐하면 스펙자체가 바뀌기 때문!!! 굉장히 큰 문제임
    // api를 만들때는 엔티티는 파라미터로 절대 받지 말 것!

    /**
     * 회원 등록 v1
     *
     * @param member
     * @return
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    /**
     * 회원 등록 v2
     *
     * @param request
     * @return
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 목록 조회 v1
     *
     * @return
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    /**
     * 회원 목록 조회 v2
     *
     * @return
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList());
        //List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m)).collect(Collectors.toList());

        return new Result(collect);

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;

    }

    // DTO
    @Data
    static class CreateMemberRequest {
        private String name;

    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
