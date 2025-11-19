package net.happykoo.ecb.api.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.domain.product.Product;
import net.happykoo.ecb.api.domain.product.ProductNotFoundException;
import net.happykoo.ecb.api.repository.ProductRepository;
import net.happykoo.ecb.api.service.dto.ProductResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResult findProduct(String productId) {
    return ProductResult.from(findProductById(productId));
  }

  public Page<ProductResult> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable)
        .map(ProductResult::from);
  }

  @Transactional
  public void decreaseStock(String productId, int stockQuantity) {
    Product product = findProductById(productId);
    product.decreaseStock(stockQuantity);
    productRepository.save(product);
  }

  @Transactional
  public void increaseStock(String productId, int stockQuantity) {
    Product product = findProductById(productId);
    product.increaseStock(stockQuantity);
    productRepository.save(product);
  }

  private Product findProductById(String productId) {
    return productRepository.findById(productId)
        .orElseThrow(ProductNotFoundException::new);
  }
}
