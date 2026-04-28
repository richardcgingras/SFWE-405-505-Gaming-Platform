const BASE_URL = "http://localhost:8080/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";

/**
 * Developer API Client for Gaming Platform
 */

// Get all developers
export async function getAllDevelopers() {
    const response = await fetch(`${BASE_URL}/developers`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Get developer by ID
export async function getDeveloperById(developerId) {
    const response = await fetch(`${BASE_URL}/developers/${developerId}`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Create new developer
export async function createDeveloper(developer) {
    const response = await fetch(`${BASE_URL}/developers`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(developer)
    });
    return response.json();
}

// Add game to developer
export async function addGameToDeveloper(developerId, gameId) {
    const response = await fetch(`${BASE_URL}/developers/${developerId}/games/${gameId}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}

// Get developer's games
export async function getDeveloperGames(developerId) {
    const response = await fetch(`${BASE_URL}/developers/${developerId}/games`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
    });
    return response.json();
}
