package net.happykoo.ecb.api.repository;

import net.happykoo.ecb.api.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
