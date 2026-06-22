package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest
{
    static ProductRepository productRepository;

    public ProductServiceTest(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Test
    public void searchShouldNotDisplayOnlyFeaturedGames()//pass is a fail
    {
        //arrange
        new ProductServiceTest()
        List<Product> products;
        ProductService productService = new ProductService(productRepository);
        //act
        products = productService.search(null, null, null, null);
        List<Product> shouldNotBeEmpty = products.stream()
                .filter(Product::isNotFeatured)
                .toList();
        //assert
        assert shouldNotBeEmpty.isEmpty(): "Test passed, list had something in it.";
    }

}