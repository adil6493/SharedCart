package com.walmart.shared_cart.controllers;

import com.walmart.shared_cart.models.Address;
import com.walmart.shared_cart.models.Item;
import com.walmart.shared_cart.models.SharedCart;
import com.walmart.shared_cart.models.User;
import com.walmart.shared_cart.service.ItemService;
import com.walmart.shared_cart.service.SharedCartService;
import com.walmart.shared_cart.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/sharedCart")
public class SharedCartController {

    @Autowired
    private SharedCartService sharedCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @GetMapping("/all")
    public Collection<SharedCart> getAllSharedCarts() {
        return sharedCartService.getAllSharedCarts();
    }

    @GetMapping("/{url}")
    public SharedCart getSharedCartDetails(@PathVariable String url) {
        return sharedCartService.getSharedCartDetails(url);
    }

    @GetMapping("/user/{userId}")
    public Collection<SharedCart> getUsersSharedCarts() {
        return sharedCartService.getAllSharedCarts();
    }

    @GetMapping("/{cartUrl}/total")
    public double getSharedCartTotal(@PathVariable String cartUrl) {
        return sharedCartService.getSharedCartTotal(cartUrl);
    }

    @PostMapping("/{userId}/{itemId}/create")
    public String createSharedCart(@PathVariable Long userId, @PathVariable int itemId) {
        User user = userService.getUser(userId);
        Item item = itemService.getItemById(itemId);
        if (user == null || item == null) {
            return "ERROR";
        }
        return sharedCartService.createSharedCart(user, item);
    }

    @PostMapping("/{cartUrl}/{userId}/add")
    public SharedCart addUserToCart(@PathVariable String cartUrl, @PathVariable Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return sharedCartService.getSharedCartDetails(cartUrl);
        }
        return sharedCartService.addUser(cartUrl, user);
    }


    @DeleteMapping("/{cartUrl}/{userId}/delete")
    public SharedCart deleteUserFromCart(@PathVariable String cartUrl, @PathVariable Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return sharedCartService.getSharedCartDetails(cartUrl);
        }
        return sharedCartService.deleteUser(cartUrl, user);
    }

    @PostMapping("/{cartUrl}/{userId}/checkout")
    public void continueToCheckout(@PathVariable String cartUrl, @PathVariable Long userId) {
        //This is invoked by current user after shared cart has been locked. This will call existing walmart API
    }

    @PostMapping("/{cartUrl}/placeOrder")
    public void placeOrder(@PathVariable String cartUrl) {
        //This is enabled when all users have paid their dues for the individual orders. Any of the user can click on this and place the order.
        //This will call existing walmart API
    }

}
