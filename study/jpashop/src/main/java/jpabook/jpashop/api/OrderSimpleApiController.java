package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
