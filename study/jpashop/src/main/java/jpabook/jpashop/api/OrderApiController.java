package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 컬렉션인 일대다 관계
 * 컬렉션은 지연 로딩으로 조회하자!!!
 * hibernate.default_batch_fetch_size: 글로벌 설정
 * @BatchSize: 개별 최적화
 *
 * (OneToMany)를 조회
 * Order -> OrderItems
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /**
     * Order 엔티티를 노출하는 방법은 좋지 않음
     * @return
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            // 강제 초기화 -> 데이터가 있으니까 실행
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());

        }
        return all;
    }

    /**
     * 껍데이 엔티티만 노출하면 안되는 것이 아니라, 속에 있는 것까지 외부에 노출하면 안됨!
     * 쿼리가 너무 많이 사용된다는 단점이 있다
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * 페이징이 안된다는 단점이 있다
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * 쿼리 호출 수가 1 + N 1 + 1 로 최적화 된다
     * 컬렉션 패치 조인은 페이징이 불가능 하지만 이는 사용 가능
     * default_batch_fetch_size 고민을 어떻게 해야할까?
     * -> 정답은 없지만 권장은 큰 숫자가 좋지만 순간 부하에 대한 고민이 된다면 100정도로 사용하는 것이 좋다
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit)
    {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit); // 페이징에 영향을 주지 않으니까!! 일단 가져오기

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }


    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        // private List<OrderItem> orderItems;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();

            /**
             * DTO안에 엔티티가 있으면 안됨!
             * 엔티티가 외부에 노출되어 버림 (의존을 완전히 끊어야됨)
             * 귀찮더라도 OrderITem조차도 DTO로 바꿔야됨!!!
             */
            // order.getOrderItems().stream().forEach(o -> o.getItem().getName());

            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto{

        private String itemName; // 상품명
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
