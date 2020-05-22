package com.example.lab_6_rpp;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

class ItemService {
    int maxId = 0;
    private List<Item> items;
    private List<DataChangedListener> listeners;
    private static final ItemService ourInstance = new ItemService();

    static ItemService getInstance() {
        return ourInstance;
    }

    public List<Item> getAvailableItems() {
        List<Item> availableItems = new LinkedList<>();
        for(Item item : items) {
            if(item.getCount() > 0)
                availableItems.add(item);
        }
        return availableItems;
    }

    public void addDataChangedListener(DataChangedListener listener) {
        listeners.add(listener);
    }

    public void addItem(Item newItem) {
        newItem.setId(maxId + 1);
        maxId +=1;
        items.add(newItem);
        listeners.forEach(new Consumer<DataChangedListener>() {
            @Override
            public void accept(DataChangedListener e) {
                e.notifyDataChanged();
            }
        });
    }

    public void deleteItem(int id) {
        for(Item item : items) {
            if(item.getId() == id)
                items.remove(item);
        }
        listeners.forEach(new Consumer<DataChangedListener>() {
            @Override
            public void accept(DataChangedListener e) {
                e.notifyDataChanged();
            }
        });
    }

    public void updateItem(Item updatedItem) {
        for(Item item :items) {
            if(item.getId() == updatedItem.getId()) {
                    items.set(items.indexOf(item), updatedItem);
                    Cart.getInstance().updateItem(updatedItem);
            }
        }
        listeners.forEach(new Consumer<DataChangedListener>() {
            @Override
            public void accept(DataChangedListener e) {
                e.notifyDataChanged();
            }
        });
    }

    public void removeListener(DataChangedListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public List<Item> getItems() {
        return items;
    }

    public void performPurchase(Cart cart) {
        for(Item item : cart.getItemsArray()) {
            item.setCount(item.getCount() - cart.getCount(item));
        }
        listeners.forEach(new Consumer<DataChangedListener>() {
            @Override
            public void accept(DataChangedListener e) {
                e.notifyDataChanged();
            }
        });
    }
    private ItemService() {
        items = new LinkedList<>();
        listeners = new LinkedList<>();
        addItem(new Item(1,"Молоко", 200, 2, "коровье, 1 литр"));
        addItem(new Item(2,"Бананы", 120, 3, "красные, 1 кг"));
        addItem(new Item(3,"Яблоки", 69, 1, "антоновка, 1 кг"));
        addItem(new Item(4,"Курица", 150, 5, "тушка, 1 кг"));
    }
}
