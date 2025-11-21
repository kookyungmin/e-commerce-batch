package net.happykoo.ecb.batch.service.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.batch.domain.product.Product;
import net.happykoo.ecb.batch.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;


  public Long countProducts() {
    return productRepository.count();
  }

  public void save(Product product) {
    productRepository.save(product);
  }

  public List<String> getProductIds() {
    return productRepository.getAllProjectIds();
  }
}
