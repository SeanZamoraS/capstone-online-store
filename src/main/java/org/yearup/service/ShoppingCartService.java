package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.List;

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

        for(CartItem item : items)
        {
            Product product = productService.getById(item.getProductId());

            ShoppingCartItem shoppingItem = new ShoppingCartItem();
            shoppingItem.setProduct(product);

            shoppingItems.add(shoppingItem);
        }

        ArrayList<Integer> markedForDeletion = new ArrayList<Integer>();

        for(int j = 0; j < shoppingItems.size(); j++)
        {
            for(int i = 0; i < shoppingItems.size(); i++)
            {
                if(i == j)
                {
                    continue;
                }
                else
                {
                    if(shoppingItems.get(j).getProductId() ==
                    shoppingItems.get(i).getProductId())
                    {
                        shoppingItems.get(j).setQuantity(
                                shoppingItems.get(j).getQuantity() + 1);
                        markedForDeletion.add(i);
                    }
                }

            }
        }

        for(int i = 0; i < shoppingItems.size(); i++)
        {

        }

        return finalCart;
    }



    // add additional methods here
}
