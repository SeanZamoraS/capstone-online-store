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

    public ShoppingCart addToCart(int productId, int userId)
    {
        HashMap<Integer, Integer> quantityList = changeCartToQuantityList(userId);
        Product newProduct = productService.getById(productId);

        quantityList.merge(productId, 1, Integer::sum);

        List<ShoppingCartItem> shoppingItems = new ArrayList<>();
        ShoppingCart finalCart = new ShoppingCart();

        for(Map.Entry<Integer, Integer> entry : quantityList.entrySet())
        {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setProduct(productService.getById(entry.getKey()));
            newItem.setQuantity(entry.getValue());
            shoppingItems.add(newItem); //add to list
        }

        shoppingItems.stream()
                .forEach(item -> finalCart.add(item));

        return finalCart;

    }

    public CartItem saveToCart(int productId, int userId)
    {
        HashMap<Integer, Integer> quantityList = changeCartToQuantityList(userId);

        quantityList.merge(productId, 1, Integer::sum);

        CartItem finalCartItem = new CartItem();
        finalCartItem.setQuantity(quantityList.get(productId));
        finalCartItem.setProductId(productId);
        finalCartItem.setUserId(userId);

        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        for(CartItem item : items)
        {
            if(item.getUserId() == userId && item.getProductId() == productId)
            {
                finalCartItem.setCartItemId(item.getCartItemId());
                return shoppingCartRepository.save(finalCartItem);
            }
        }
        //finalCartItem.setCartItemId(null);
        return  shoppingCartRepository.save(finalCartItem);

    }
    private HashMap<Integer, Integer> changeCartToQuantityList(int userId)
    {//key is product id, value is quantity
        List<CartItem> items = shoppingCartRepository.findByUserId(userId);

        //let's get a list of all the products for this user from the cart items
        List<Product> productsList = items.stream()
                .map(item -> productService.getById(item.getProductId()))
                .toList();

        //key is product id, value is quantity
        HashMap<Integer, Integer> quantityList = new HashMap<>();
        //now that we have a list of products, for each product add it to the tally (hashmap)
        productsList.stream()
                .forEach(product -> quantityList.merge(product.getProductId(), 1, Integer::sum));

        return quantityList;
    }

    public ShoppingCart editCart(ShoppingCart currentCart, int productId, int newQuantity)
    {
        Map<Integer, ShoppingCartItem> itemsMap = currentCart.getItems();

        for(Map.Entry<Integer, ShoppingCartItem> entry : itemsMap.entrySet())
        {
            if(entry.getValue().getProductId() == productId)
            {
                entry.getValue().setQuantity(newQuantity);
            }
        }
        return currentCart;
    }


}
