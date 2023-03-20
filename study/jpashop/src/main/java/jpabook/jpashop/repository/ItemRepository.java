package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){ // item 값이 없음! 새로 생성하는 것임
            em.persist(item);
        }else{ // update와 비슷하다고 생각하면 됨
            // 병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다.
            /* item에 넘어온 값으로 바꿔치기를 해버림
               병합은 조심해야됨!!!!
               왜? 모든 속성이 다 바뀌기 때문!!!
               만약에 값이 없으면 null로 다 바뀜
               merge는 사용을 지양하고 불편하더라도 변경감지를 사용하는 것이 좋음
            */
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
