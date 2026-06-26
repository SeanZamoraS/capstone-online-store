package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("cart")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_USER')")
public class ShoppingCartController
{
    private ShoppingCartService shoppingCartService;
    private UserService userService;
    @Autowired
    public ShoppingCartController(ShoppingCartService shopService, UserService userService)
    {
        this.shoppingCartService = shopService;
        this.userService = userService;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCart getCart(Principal principal)
    {
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        // use the shoppingCartService to get all items in the cart and return the cart
        ShoppingCart userCart = shoppingCartService.getByUserId(userId);
        return userCart;
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be added)
    // return the updated cart with status 201 Created
    @PostMapping("products/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ShoppingCart> addToCart(Principal principal, @PathVariable int id)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        CartItem cartItem = shoppingCartService.saveToCart(id, userId);

        ShoppingCart userCart = shoppingCartService.getByUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCart);
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    @PutMapping("products/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ShoppingCart> editQuantityItem(Principal principal, @PathVariable int productId,
    @RequestBody int quantity)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        ShoppingCart currentCart = shoppingCartService.getByUserId(userId);
        ShoppingCart newCart = shoppingCartService.editCart(currentCart, productId, quantity, userId);

        if(newCart == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(currentCart);
        }

        return ResponseEntity.status(HttpStatus.OK).body(newCart);
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
    @DeleteMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        boolean successful = shoppingCartService.deleteCart(userId);

        if(!successful)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }

}
