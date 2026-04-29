const BASE_URL = "http://localhost:8080/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";

/**
 * User Profile API Client for Gaming Platform
 */

// Get user profile by ID
export async function getUserProfile(userId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/user-profiles/${userId}`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    if (!response.ok) {
        throw new Error("Failed to fetch user profile");
    }
    return response.json();
}

// Get user profile by Username
export async function getUserProfileByUsername(username) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/user-profiles/username/${username}`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    if (!response.ok) {
        throw new Error("Failed to fetch user profile by username");
    }
    return response.json();
}

// Add friend
export async function addFriend(userId, friendId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/user-profiles/${userId}/friends/${friendId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}
