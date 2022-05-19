package com.walmart.shared_cart.service;

import com.walmart.shared_cart.dao.SharedCartRepo;
import com.walmart.shared_cart.models.Item;
import com.walmart.shared_cart.models.SharedCart;
import com.walmart.shared_cart.models.User;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SharedCartService {

    private static final String BASE_URL = "http://localhost:3000/";

    @Autowired
    SharedCartRepo sharedCartRepo;

    public Collection<SharedCart> getAllSharedCarts() {
        return sharedCartRepo.getAll();
    }

    public SharedCart getSharedCartDetails(String url) {
        return sharedCartRepo.getById(url);
    }

    public Collection<SharedCart> getByUserId(Long userId) {
        return sharedCartRepo.getAll().stream().filter(sc -> sc.getOwner().getUserId().equals(userId)).collect(Collectors.toList());
    }

    public String createSharedCart(User user, Item item) {
        String uniqueUrl = RandomStringUtils.randomAlphanumeric(7);
        List<User> members = new ArrayList<>();
        user.getUserItems().add(item);
        user.setTotalAmount(item.getPrice());
        user.setSharedCartUrl(uniqueUrl);
        members.add(user);
        double cartTotal = item.getPrice();
        SharedCart sharedCart = new SharedCart(uniqueUrl, "Default Name", user, members,cartTotal, user.getAddress().getZipCode(), 1);
        sharedCartRepo.addSharedCartDetails(uniqueUrl, sharedCart);
        return BASE_URL + uniqueUrl;
    }

    public SharedCart addUser(String cartUrl, User user) {
        SharedCart sharedCart = getSharedCartDetails(cartUrl);

        if (sharedCart == null || sharedCart.getTotalMembers() >= 3) {
            System.out.println("Maximum allowed users for shared cart are 3");
            return sharedCartRepo.addSharedCartDetails(cartUrl, sharedCart);
        }

        List<User> members = sharedCart.getCartMembers();
        for (User member : members) {
            if (member.getUserId().equals(user.getUserId())) {
                System.out.println("Existing user");
                return sharedCart;
            }
        }
        int sharedCartPinCode = sharedCart.getZipcode();
        int userZipCode = user.getAddress().getZipCode();
        if (sharedCartPinCode == userZipCode) {
            user.setSharedCartUrl(cartUrl);
            sharedCart.getCartMembers().add(user);
        }
        return sharedCartRepo.addSharedCartDetails(cartUrl, sharedCart);
    }

    public SharedCart deleteUser(String cartUrl, User user) {
        SharedCart sharedCart = getSharedCartDetails(cartUrl);

        if (sharedCart == null) {
            System.out.println("Shared Cart with id:" + cartUrl + "does not exist");
            return sharedCartRepo.addSharedCartDetails(cartUrl, sharedCart);
        }

        List<User> members = sharedCart.getCartMembers();
        if (members.stream().anyMatch(x -> x.getUserId() == user.getUserId())) {
            sharedCart.getCartMembers().remove(user);
        }
        return sharedCartRepo.addSharedCartDetails(cartUrl, sharedCart);
    }


    public double getSharedCartTotal(String cartUrl) {
        SharedCart sharedCart = sharedCartRepo.getById(cartUrl);
        return sharedCart.getCartTotal();
    }
}
