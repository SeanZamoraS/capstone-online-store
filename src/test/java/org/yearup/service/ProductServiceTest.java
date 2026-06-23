package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest //learned I could use JPA annotations with tests
@Sql(scripts = "classpath:create_database_videogamestore.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductServiceTest
{
    @Autowired
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUpRepo()
    {
        this.productService = new ProductService(productRepository);
    }

    @Test
    public void searchShouldNotDisplayOnlyFeaturedGames()
    {
        //arrange
        List<Product> products;

        //act
        products = productService.search(null, null, null, null);
        List<Product> shouldNotBeEmpty = products.stream()
                .filter(Product::isNotFeatured)
                .toList();
        //assert
        assertFalse(shouldNotBeEmpty.isEmpty(), "list was empty, only featured products appeared");
    }

    @Test
    public void updateShouldChangeStock()
    {
        //arrange
        Product testProduct = new Product(1000, "test", 100.00, 3, "this is a test", "TEST", 100, false, "none");
        Product savedProduct = productService.create(testProduct);
        //act
        Product compareProduct = new Product(savedProduct.getProductId(), savedProduct.getName(),
                savedProduct.getPrice(), savedProduct.getCategoryId(), savedProduct.getDescription(),
                savedProduct.getSubCategory(), 75, savedProduct.isFeatured(), savedProduct.getImageUrl());
        Product updatedProduct = productService.update(savedProduct.getProductId(), compareProduct);

        //had to do this jank because it would have been the same object in memory otherwise and thus passing everytime (?)
        //assert
        assertEquals(75, updatedProduct.getStock(), "Expected 75, got 100");
    }
}