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
}