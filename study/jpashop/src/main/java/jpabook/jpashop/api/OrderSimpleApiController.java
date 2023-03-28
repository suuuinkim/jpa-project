package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x To One(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * 성능의 문제가 있음
     * 사용하지 않는 category, orderItems를 가지고 옴
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(Model model){
        // 지연로딩이기 때문에 db에서 가지고 오지 않음! 프록시 객체를 만들어서 넣어둠
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        // force_lazy_loding 끄고 내가 원하는 값만 뿌려주는 방법
        for (Order order : all) {
            order.getMember().getName(); // order.getMember() 까지는 프록시 객체 -> 즉, 진짜가 아님 -> 쿼리가 안날라감 -> getName()까지 하면 Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }

        return all;
    }

    /**
     * Lazy 로딩으로 인한 데이터를 너무 많이 조회한다는 단점이 있다(v1 동일)
     *
     * ORDER -> SQL 1번 -> 결과 주문수 2개
     * N+1의 문제 발생
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2(){
        // ORDER 2개
        // N+1 -> 1 + 회원N + 배송N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
