package com.cassiomolin.example.product.service;

import com.cassiomolin.example.product.model.Product;
import com.cassiomolin.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service that provides the a greeting.
 *
 * @author cassiomolin
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    @Qualifier(ProductOutput.PRODUCT_OUTPUT)
    private MessageChannel messageChannel;

    public String createProduct(Product product) {
        product = productRepository.save(product);
        return product.getId();
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProduct(String id) {
        Product one = productRepository.findOne(id);
        return Optional.ofNullable(one);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findOne(id);
        if (product != null) {
            productRepository.delete(id);
            messageChannel.send(MessageBuilder.withPayload(product).build());
        }
    }
}