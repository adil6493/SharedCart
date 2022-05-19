package com.walmart.shared_cart.controllers;

import com.walmart.shared_cart.models.Address;
import com.walmart.shared_cart.models.Item;
import com.walmart.shared_cart.models.SharedCart;
import com.walmart.shared_cart.models.User;
import com.walmart.shared_cart.service.ItemService;
import com.walmart.shared_cart.service.SharedCartService;
import com.walmart.shared_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SharedCartService sharedCartService;

    @GetMapping
    public Collection<User> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/{userId}/address")
    public Address getUserAddress(@PathVariable Long userId) {
        return userService.getUser(userId).getAddress();
    }

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/delete/{userId}")
    public User removeUser(@PathVariable int userId) { return userService.deleteUser(userId);}

    @PostMapping("/{userId}/pay")
    public void payBill(@PathVariable Long userId) {
        //After checkout from sharedCart each user can pay their individual cart total. Existing walmart API is called
    }

    @GetMapping("/{orderId}/trackOrder")
    public void getOrderStatus(@PathVariable String orderId) {
        //After the order has been placed from shared cart each user can track their individual orders. Existing walmart API is called.
    }

    @PostMapping("/{userId}/{itemId}/add")
    public Item addItem(@PathVariable Long userId, @PathVariable int itemId) {
        User user = userService.getUser(userId);
        Item item = userService.addItemForUser(userId, itemId);
        SharedCart sharedCart = sharedCartService.getSharedCartDetails(user.getSharedCartUrl());
        double total = sharedCartService.getSharedCartTotal(sharedCart.getCartUrl());
        sharedCart.setCartTotal(total + item.getPrice());
        return item;
    }

    @DeleteMapping("/{userId}/{itemId}/delete")
    public Item deleteItem(@PathVariable Long userId, @PathVariable int itemId) {
        User user = userService.getUser(userId);
        Item item = userService.deleteItemForUser(userId, itemId);
        SharedCart sharedCart = sharedCartService.getSharedCartDetails(user.getSharedCartUrl());
        double total = sharedCartService.getSharedCartTotal(sharedCart.getCartUrl());
        sharedCart.setCartTotal(total - item.getPrice());
        return item;
    }
}
