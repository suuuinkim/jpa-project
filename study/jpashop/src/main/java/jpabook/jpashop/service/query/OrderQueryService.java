package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OSIV를 끈 상태로 복잡성을 관리하는 방법
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderRepository orderRepository;
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

}
