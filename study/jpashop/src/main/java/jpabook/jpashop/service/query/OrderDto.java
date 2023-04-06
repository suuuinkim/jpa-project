package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto{
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private List<OrderApiController.OrderItemDto> orderItems;
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
                .map(orderItem -> new OrderApiController.OrderItemDto(orderItem))
                .collect(Collectors.toList());
    }
}

