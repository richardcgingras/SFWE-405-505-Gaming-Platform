const API_BASE = "http://localhost:8080/api";

function authHeaders() {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
}

export async function getCurrentUser() {
  const username = localStorage.getItem("username");
  const response = await fetch(`${API_BASE}/user-profiles/username/${username}`, {
    headers: authHeaders(),
  });

  if (!response.ok) throw new Error("Unable to load current user");
  return response.json();
}

export async function getUserProfileById(id) {
  const response = await fetch(`${API_BASE}/user-profiles/${id}`, {
    headers: authHeaders(),
  });

  if (!response.ok) throw new Error("Profile not found");
  return response.json();
}

export async function sendFriendRequest(senderId, receiverId) {
  const response = await fetch(`${API_BASE}/friend-requests`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify({ senderId, receiverId }),
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "Unable to send request");
  }

  return response.json();
}

export async function getReceivedRequests(userId) {
  const response = await fetch(`${API_BASE}/friend-requests/received/${userId}`, {
    headers: authHeaders(),
  });

  if (!response.ok) throw new Error("Unable to load incoming requests");
  return response.json();
}

export async function acceptFriendRequest(requestId) {
  const response = await fetch(`${API_BASE}/friend-requests/${requestId}/accept`, {
    method: "POST",
    headers: authHeaders(),
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "Unable to accept request");
  }

  return response.text();
}

export async function denyFriendRequest(requestId) {
  const response = await fetch(`${API_BASE}/friend-requests/${requestId}/deny`, {
    method: "POST",
    headers: authHeaders(),
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "Unable to deny request");
  }

  return response.text();
}