const BASE_URL = "http://localhost:8080/api/users";

export async function login(username, password) {
    const response = await fetch(`${BASE_URL}/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            username,
            password,
        }),
    });
    if (!response.ok) {
        throw new Error("Invalid Login");
    }

    return response.json();
}