const BASE_URL = "http://localhost:8080/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";

/**
 * Review API Client for Gaming Platform
 */

// Submit review
export async function submitReview(userId, gameId, comments, rating) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/reviews/submit`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            userId,
            gameId,
            comments,
            rating
        })
    });
    return response.json();
}

// Get reviews by game
export async function getReviewsByGame(gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/reviews/game/${gameId}`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    return response.json();
}

// Get average review rating by game
export async function getAverageReviewByGame(gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/reviews/game/score/${gameId}`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    return response.json();
}