const BASE_URL = "http://localhost:8080/api";

/**
 * Shopping Cart API Client for Gaming Platform
 */

// Add game to shopping cart
export async function addGameToCart(cartId, gameId) {
    const response = await fetch(`${BASE_URL}/shopping-cart/game/${cartId}/${gameId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    });
    return response.json();
}

// Remove game from shopping cart
export async function removeGameFromCart(cartId, gameId) {
    const response = await fetch(`${BASE_URL}/shopping-cart/game/${cartId}/${gameId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
    });
    return response.json();
}

// Get total price for shopping cart
export async function getCartTotal(cartId) {
    const response = await fetch(`${BASE_URL}/shopping-cart/total/${cartId}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    });
    return response.json();
}

// Credit card checkout
export async function checkoutWithCreditCard(cartId, destinationAccountId, paymentObject) {
    const response = await fetch(`${BASE_URL}/shopping-cart/checkout/creditcard/${cartId}/${destinationAccountId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(paymentObject)
    });
    return response.json();
}

// Debit card checkout
export async function checkoutWithDebitCard(cartId, destinationAccountId, paymentObject) {
    const response = await fetch(`${BASE_URL}/shopping-cart/checkout/debitcard/${cartId}/${destinationAccountId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(paymentObject)
    });
    return response.json();
}

// Gift card checkout
export async function checkoutWithGiftCard(cartId, destinationAccountId, paymentObject) {
    const response = await fetch(`${BASE_URL}/shopping-cart/checkout/giftcard/${cartId}/${destinationAccountId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(paymentObject)
    });
    return response.json();
}