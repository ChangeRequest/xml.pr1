package com.github.changerequest.store.api;

import com.github.changerequest.store.model.Basket;
import com.github.changerequest.store.model.Item;

import java.util.List;
import java.util.Map;

public interface BasketApi {
    /**
     * Add item to the basket
     *
     * @param basket basket to add item to
     * @param item   item to add to the basket
     * @throws NullPointerException if basket is null
     */
    void add(Basket basket, Item item) throws NullPointerException;

    /**
     * Return latest {@code n} items from the basket or all items from the basket
     * if less than {@code n} items is available.
     *
     * @param basket the basket to look for items in
     * @param n      amount of items to look up
     * @return list of {@code n} or {@code basket.size()} items from the {@code basket}
     * @throws NullPointerException     if {@code basket} is null
     * @throws IllegalArgumentException if {@code n} is smaller than 1
     */
    List<Item> getLast(Basket basket, int n);

    /**
     * Remove item from the basket.
     * The order of removed items is not guaranteed.
     *
     * @param basket basket to remove item from
     * @param item   item to be removed
     * @throws NullPointerException if basket is null
     */
    void remove(Basket basket, Item item);

    Map<Item, Integer> checkout(Basket basket);
}
