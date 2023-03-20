package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    /**
     * 변경 감지 기능
     */
    @Transactional
    public void updateItem(Long itemId, Book param){
        Item findItem = itemRepository.findOne(itemId); // 영속상태
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity()); // 값 세팅
        // ==== transactional에 의해서 커밋이 됨 ===
        // 그럼 jpa는 플러시를 날림 -> 영속성컨테이너에서 변경된 애가 뭔지 다 찾음!!!
        // 뭐가 바꼇네???
        // 변경감지에 의해서 update 침
    }

    // 병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다. 
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);

    }
}
