package net.happykoo.ecb.api.repository;

import net.happykoo.ecb.api.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
