package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{
        Book book = em.find(Book.class, 1L);

        // TX
        book.setName("test");

        // 변경감지 == dirty checking
        /*준영속 엔티티?
        영속성 컨테이너가 더는 관리하지 않는 엔티티
        문제? JPA가 관리를 하지 않음!!!
        그럼 어떻게 관리를 할 수 있을까?

        방법 1) 변경 감지 기능 사용
        - dirty checking
        방법 2) 병합('merge') 사용

        */


        // TX commit

    }
}
