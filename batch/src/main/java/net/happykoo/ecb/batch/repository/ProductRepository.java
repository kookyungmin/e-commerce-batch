package net.happykoo.ecb.batch.repository;


import java.util.List;
import net.happykoo.ecb.batch.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {

  @Query("select p.productId from Product p")
  List<String> getAllProjectIds();

}
