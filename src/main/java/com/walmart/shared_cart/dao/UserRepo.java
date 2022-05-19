package com.walmart.shared_cart.dao;

import com.walmart.shared_cart.models.Address;
import com.walmart.shared_cart.models.Item;
import com.walmart.shared_cart.models.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class UserRepo {

    Map<Long, User> userMap;
    Long currUserId;
    Long currAddressId;

    @PostConstruct
    private void postConstruct() {
        this.userMap = new HashMap<>();
        this.currUserId = 10000L;
        this.currAddressId = 800L;
        List<Item> chandlerItems = new ArrayList<>();

        User chandler = new User(currUserId, "Chandler", "Bing",
                new Address(currAddressId, currUserId, "90", "Bedford Street", "New York", "New York", "US", 10028), chandlerItems, null, 0);

        userMap.put(currUserId, chandler);
        this.currUserId++;
        this.currAddressId++;
        List<Item> monicaItems = new ArrayList<>();
        User monica = new User(currUserId, "Monica", "Geller",
                new Address(currAddressId, currUserId, "91", "Bedford Street", "New York", "New York", "US", 10028), monicaItems, null, 0);

        userMap.put(currUserId, monica);
        this.currUserId++;
        this.currAddressId++;
        List<Item> rachelItems = new ArrayList<>();
        User rachel = new User(currUserId, "Rachel", "Green",
                new Address(currAddressId, currUserId, "91", "Bedford Street", "New York", "New York", "US", 10028), rachelItems, null, 0);

        userMap.put(currUserId, rachel);
        this.currUserId++;
        this.currAddressId++;
    }

    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    public User getUserById(Long userId) {
        return userMap.get(userId);
    }

    public User addUser(User user) {
        currUserId++;
        currAddressId++;
        user.setUserId(currUserId);
        user.getAddress().setUserId(currUserId);
        user.getAddress().setAddressId(currAddressId);

        this.userMap.put(currUserId, user);
        return userMap.get(currUserId);
    }

    public User deleteUser(long userId) {
        User user;
        if (!userMap.containsKey(userId)) {
            throw new RuntimeException("Unable to find the user to be deleted");
        }
        user = userMap.get(userId);
        userMap.remove(userId);
        this.currUserId--;
        return user;
    }
}
