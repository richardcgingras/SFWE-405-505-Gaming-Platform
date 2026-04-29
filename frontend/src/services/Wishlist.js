const BASE_URL = "http://localhost:8080/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";

/**
 * Wishlist API Client for Gaming Platform
 */

// get all games in wishlist
export async function getWishlistGames() {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/wishlist`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    if (!response.ok) {
        throw new Error("Failed to fetch wishlist");
    }
    return response.json();
}

// Add game to wishlist
export async function addGameToWishlist(gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/wishlist/game/${gameId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Remove game from wishlist
export async function removeGameFromWishlist(gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/wishlist/game/${gameId}`, {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Get wishlist total price
export async function getWishlistTotal() {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/wishlist/total`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}
