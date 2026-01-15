package com.upb.agripos.controller;

import com.upb.agripos.service.CartService;
import com.upb.agripos.model.TransactionItem;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;

/**
 * Controller for managing shopping cart display and operations.
 */
public class CartController {
    @FXML private TableView<TransactionItem> tableCart;
    private final CartService cartService = new CartService();

    /**
     * Handles removal of selected item from cart.
     * Shows alert if no item is selected.
     */
    @FXML
    private void handleRemoveItem() {
        TransactionItem selected = tableCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartService.removeItem(selected);
            tableCart.getItems().remove(selected);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select an item to remove.");
            alert.showAndWait();
        }
    }

    /**
     * Refreshes the cart display from the service.
     */
    public void refreshCart() {
        if (tableCart != null) {
            tableCart.getItems().setAll(cartService.getCartItems());
        }
    }
}