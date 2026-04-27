const BASE_URL = "http://localhost:8080/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";
const getId = () => localStorage.getItem("userId") || "";

/**
 * Shopping Cart API Client for Gaming Platform
 */

// get all games in shopping cart
export async function getGames() {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/shopping-cart`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Add game to shopping cart
export async function addGameToCart(gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/shopping-cart/game/${gameId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Remove game from shopping cart
export async function removeGameFromCart(gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/shopping-cart/game/${gameId}`, {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Get total price for shopping cart
export async function getCartTotal() {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/shopping-cart/total`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// TODO: API supports gifting bt specifying a different account, but the UI is not able to handle this use case yet
// Credit card checkout
export async function checkoutWithCreditCard(paymentObject) {
    const token = getToken();
    const userId = getId();
    const response = await fetch(`${BASE_URL}/shopping-cart/checkout/creditcard/${userId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paymentObject)
    });
    return response.json();
}

// Debit card checkout
export async function checkoutWithDebitCard(paymentObject) {
    const token = getToken();
    const userId = getId();
    const response = await fetch(`${BASE_URL}/shopping-cart/checkout/debitcard/${userId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paymentObject)
    });
    return response.json();
}

// Gift card checkout
export async function checkoutWithGiftCard(paymentObject) {
    const token = getToken();
    const userId = getId();
    const response = await fetch(`${BASE_URL}/shopping-cart/checkout/giftcard/${userId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paymentObject)
    });
    return response.json();
}