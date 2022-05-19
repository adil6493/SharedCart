package com.walmart.shared_cart.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {

    private Long userId;

    private String firstName;

    private String lastName;

    private Address address;

    private List<Item> userItems;

    private String sharedCartUrl;

    private double totalAmount;
}
