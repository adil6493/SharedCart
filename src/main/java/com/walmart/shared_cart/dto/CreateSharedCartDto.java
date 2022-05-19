package com.walmart.shared_cart.dto;

public class CreateSharedCartDto {
    private String cartName;
    private int itemId;


    public CreateSharedCartDto() {
    }

    public CreateSharedCartDto(String cartName, int itemId) {
        this.cartName = cartName;
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }
}
