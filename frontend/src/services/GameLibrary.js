const BASE_URL = "http://localhost:8080/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";

/**
 * Game Library API service for the Gaming Platform frontend.
 */

export async function getAllLibraries() {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch libraries: ${response.statusText}`);
    }
    return response.json();
}

export async function getLibraryById(id) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${id}`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch library by ID: ${response.statusText}`);
    }
    return response.json();
}

export async function createLibrary(library) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json',},
        body: JSON.stringify(library),
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to create library: ${response.statusText}`);
    }
    return response.json();
}

export async function getGames(libraryId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${libraryId}/games`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch games for library: ${response.statusText}`);
    }
    return response.json();
}

export async function addGame(libraryId, gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${libraryId}/games/${gameId}`, {
        method: 'GET',
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to add game: ${response.statusText}`);
    }
    return response.json();
}

export async function removeGame(libraryId, gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${libraryId}/games/${gameId}`, {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to remove game: ${response.statusText}`);
    }
    return response.json();
}

export async function hasGame(libraryId, gameId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${libraryId}/games/${gameId}/has`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to check game existence: ${response.statusText}`);
    }
    return response.json();
}

export async function getTotalSize(libraryId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${libraryId}/totalsize`, {
        method: 'GET',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to get total size: ${response.statusText}`);
    }
    return response.json();
}

export async function deleteLibrary(libraryId) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/gamelibrary/${libraryId}`, {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to delete library: ${response.statusText}`);
    }
    return response.json();
}
