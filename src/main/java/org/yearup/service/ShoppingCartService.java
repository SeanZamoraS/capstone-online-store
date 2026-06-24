package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        List<ShoppingCartItem> shoppingItems = new ArrayList<>();

        ShoppingCart finalCart = new ShoppingCart();

        //let's get a list of all the products for this user from the cart items
        List<Product> productsList = items.stream()
                .map(item -> productService.getById(item.getProductId()))
                .toList();

        //key is product id, value is quantity
        HashMap<Integer, Integer> quantityList = new HashMap<>();
        //now that we have a list of products, for each product add it to the tally (hashmap)
        productsList.stream()
                .forEach(product -> quantityList.merge(product.getProductId(), 1, Integer::sum));

        //now let's take our product and quantity and make it into a ShoppingCartItem proper
        for(Map.Entry<Integer, Integer> entry : quantityList.entrySet())
        {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setProduct(productService.getById(entry.getKey()));
            newItem.setQuantity(entry.getValue());
            shoppingItems.add(newItem); //add to list
        }

        //add each ShoppingCartItem into the cart
        shoppingItems.stream()
                .forEach(item -> finalCart.add(item));

        return finalCart;
    }





    // add additional methods here
}
