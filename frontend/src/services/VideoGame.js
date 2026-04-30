const BASE_URL = "/api";

// Helper to get token from localStorage
const getToken = () => localStorage.getItem("token") || "";

/**
 * Video Game API service for the Gaming Platform frontend.
 */

/**
 * Retrieves all video games.
 * @returns {Promise<Array>} - A promise that resolves to an array of video games.
 */
export async function getAllVideoGames() {
    const response = await fetch(`${BASE_URL}/video-games`, {
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
    }
    return response.json();
}

/**
 * Retrieves a video game by ID.
 * @param {number} id - The ID of the video game.
 * @returns {Promise<Object>} - A promise that resolves to the video game object.
 */
export async function getVideoGameById(id) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/video-games/${id}`,{
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }});
    if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
    }
    return response.json();
}

/**
 * Creates a new video game.
 * @param {Object} videoGame - The video game data to create.
 * @returns {Promise<Object>} - A promise that resolves to the created video game object.
 */
export async function createVideoGame(videoGame) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/video-games`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(videoGame),
    });
    if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
    }
    return response.json();
}

/**
 * Retrieves a video game file by name.
 * @param {number} id - The Video game ID.
 * @param {string} file - The file name.
 * @returns {Promise<Blob|null>} - A promise that resolves to the file blob or null on 404.
 */
export async function getFile(id, file) {
    const token = getToken();
    const response = await fetch(`${BASE_URL}/video-games/download?id=${id}&file=${encodeURIComponent(file)}`,{
        headers: {
            Authorization: `Bearer ${token}`
        }});
    
    // Handle 404 as requested (return null instead of throwing)
    if (response.status === 404) {
        return null;
    }
    
    if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
    }
    
    return response.blob();

}

/**
 * Uploads a file for a video game.
 * @param {number} id - The Video game ID.
 * @param {File} file - The file to upload.
 * @returns {Promise<Object>} - A promise that resolves to the response body or error.
 */
export async function uploadFile(id, file) {
    const token = getToken();
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${BASE_URL}/video-games/upload/${id}`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`
        },// Note: Don't set Content-Type for FormData, browser sets it automatically with boundary
        body: formData
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Upload failed: ${response.status} - ${errorText}`);
    }

    return response.blob();
}