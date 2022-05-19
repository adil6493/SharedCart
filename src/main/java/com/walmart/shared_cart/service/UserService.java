package com.walmart.shared_cart.service;

import com.walmart.shared_cart.dao.ItemsRepo;
import com.walmart.shared_cart.dao.SharedCartRepo;
import com.walmart.shared_cart.dao.UserRepo;
import com.walmart.shared_cart.models.Item;
import com.walmart.shared_cart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ItemsRepo itemsRepo;

    public Collection<User> getAllUsers() {
        return userRepo.getAllUsers();
    }

    public User getUser(Long userId) {
        return userRepo.getUserById(userId);
    }

    public User addUser(User user) {
        return userRepo.addUser(user);
    }

    public Item addItemForUser(Long userId, int itemId) {
        User user = userRepo.getUserById(userId);
        Item item = itemsRepo.getItemById(itemId);
        double totalAmount;
        if (user == null || item == null) {
            return null;
        }
        List<Item> userItems = user.getUserItems();
        if (userItems.stream().anyMatch(x->x.getId() == item.getId())) {
            item.setItemCount(item.getItemCount() + 1);
            totalAmount = user.getTotalAmount();
            user.setTotalAmount(totalAmount + item.getPrice());
        }
        else {
            user.getUserItems().add(item);
            totalAmount = user.getTotalAmount();
            user.setTotalAmount(totalAmount + item.getPrice());
        }
        return item;
    }

    public Item deleteItemForUser(Long userId, int itemId) {
        User user = userRepo.getUserById(userId);
        Item item = itemsRepo.getItemById(itemId);
        double totalAmount;
        if (user == null || item == null) {
            return null;
        }
        List<Item> userItems = user.getUserItems();
        if (userItems.stream().anyMatch(x->x.getId() == item.getId())) {
            if (item.getItemCount() > 0) {
                item.setItemCount(item.getItemCount() - 1);
            }
            totalAmount = user.getTotalAmount();
            user.setTotalAmount(totalAmount - item.getPrice());
        }
        else {
            user.getUserItems().remove(item);
            totalAmount = user.getTotalAmount();
            user.setTotalAmount(totalAmount - item.getPrice());
        }
        return item;
    }

    public User deleteUser(long userId) {
        User user = null;
        try {
            user = userRepo.getUserById(userId);
            userRepo.deleteUser(userId);
        } catch(Exception e) {
            System.out.println("Failed to delete user" +e);
        }
        return user;
    }
}
