package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


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
    private final OrderQueryRepository orderQueryRepository;

    /**
     * 권장 순서
     * 1. 엔티티 조회 방식으로 우선 접근
     *  1) 패치조인으로 쿼리 수를 최적화
     *  2) 컬렉션 최적화
     *     - 페이징 필요 hibernate.default_batch_fetch_size, @BatchSize 로 최적화
     *     - 페이징 필요 x -> 패치 조인 사용
     *
     *  2. 엔티티 조회 방식으로 해결이 안되면 DTO 조회 방식 아용
     *  3. DTO 조회방식으로 해결이 안되면 NativeSQL 이나 JdbcTemplate
     */

    /**
     * Order 엔티티를 노출하는 방법은 좋지 않음
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
     * 껍데기 엔티티만 노출하면 안되는 것이 아니라, 속에 있는 것까지 외부에 노출하면 안됨!
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
     * OSIV를 끈 상태로 복잡성을 관리하는 방법
     *
     * 장점) 지연로딩을 고민하지 않아도 된다, 코딩만 생각한다면 장점이 더 많다. (켰을때)
     * 단점) 성능을 생각한다면 끄는게 맞음
     *
     * 실시간 API는 OSIV를 끄고, ADMIN 처럼 커넥션을 많이 사용하지 않는 곳에선s OSIV를 켠다.
     */
//    private final OrderQueryService orderQueryService;
//    @GetMapping("/api/v3/orders")
//    public List<jpabook.jpashop.service.query.OrderDto> ordersV3(){
//
//        return orderQueryService.ordersV3();
//        List<Order> orders = orderRepository.findAllWithItem();
//
//        List<OrderDto> collect = orders.stream()
//                .map(o -> new OrderDto(o))
//                .collect(Collectors.toList());
//
//        return collect;
//    }

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

    /**
     * JPA에서 DTO를 직접 조회
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    /**
     * 컬렉션 조회 최적화 - 일대다 관계인 컬렉션은 IN 절을 활용해서 메모리에 미리 조회해서 최적화
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();

    }

    /**
     * 플랫 데이터 최적화 - JOIN 결과를 그대로 조회 후 애플리케이션에서 원하는 모양으로 직접 변환
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6(){
        // return orderQueryRepository.findAllByDto_flat(); 스펙 맞추기 전

        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        // flat을 가지고 루프를 돌리는 것
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());

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
    public static class OrderItemDto{

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
