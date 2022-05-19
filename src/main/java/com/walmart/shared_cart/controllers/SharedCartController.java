package com.walmart.shared_cart.controllers;

import com.walmart.shared_cart.dto.CreateSharedCartDto;
import com.walmart.shared_cart.models.Item;
import com.walmart.shared_cart.models.SharedCart;
import com.walmart.shared_cart.models.User;
import com.walmart.shared_cart.service.ItemService;
import com.walmart.shared_cart.service.SharedCartService;
import com.walmart.shared_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/sharedCart")
@CrossOrigin(origins = "http://localhost:3000")
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
    public Collection<SharedCart> getUsersSharedCarts(@PathVariable Long userId) {
        List<SharedCart> userShareCarts = new ArrayList<>();
        Collection<SharedCart> allSharedCarts = sharedCartService.getAllSharedCarts();
        for (SharedCart cart : allSharedCarts) {
            for (User member : cart.getCartMembers()) {
                if (member.getUserId().equals(userId)) {
                    userShareCarts.add(cart);
                    break;
                }
            }
        }
        return userShareCarts;
    }

    @PostMapping("/{userId}/create")
    public String createSharedCart(@PathVariable Long userId, @RequestBody CreateSharedCartDto dto) {
        User user = userService.getUser(userId);
        Item item = itemService.getItemById(dto.getItemId());
        if (user == null || item == null) {
            return "ERROR";
        }
        return sharedCartService.createSharedCart(user, item, dto.getCartName());
    }

    @PostMapping("/{cartUrl}/{userId}/add")
    public SharedCart addUserToCart(@PathVariable String cartUrl, @PathVariable Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return sharedCartService.getSharedCartDetails(cartUrl);
        }
        return sharedCartService.addUser(cartUrl, user);
    }

    @PostMapping("/{cartUrl}/add")
    public SharedCart addItemToCart(@PathVariable String cartUrl, @RequestParam int itemId) {
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return sharedCartService.getSharedCartDetails(cartUrl);
        }
        return sharedCartService.addItem(cartUrl, item);
    }

    @DeleteMapping("/{cartUrl}/{userId}/delete")
    public SharedCart deleteUserFromCart(@PathVariable String cartUrl, @PathVariable Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return sharedCartService.getSharedCartDetails(cartUrl);
        }
        return sharedCartService.deleteUser(cartUrl, user);
    }

    @DeleteMapping("/{cartUrl}/delete")
    public SharedCart deleteUserFromCart(@PathVariable String cartUrl, @RequestParam int itemId) {
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return sharedCartService.getSharedCartDetails(cartUrl);
        }
        return sharedCartService.deleteItem(cartUrl, item);
    }

}
