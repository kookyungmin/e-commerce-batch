package net.happykoo.ecb.api.repository;

import net.happykoo.ecb.api.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
