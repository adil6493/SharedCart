package com.walmart.shared_cart.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SharedCart {
    private String cartUrl;

    private String cartName;

    private User owner;

    private List<User> cartMembers;

    private double cartTotal;

    private int zipcode;

    private int totalMembers;

    public int getTotalMembers() {
        return cartMembers.size();
    }
}
