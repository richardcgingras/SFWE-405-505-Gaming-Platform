const BASE_URL = "http://localhost:8080/api";

export async function purchaseGame({ gameId, email }) {
  const response = await fetch(`${BASE_URL}/purchase`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      gameId,
      email,
    }),
  });

  if (!response.ok) {
    throw new Error("Purchase failed");
  }

  return response.json();
}

export async function addToLibrary({ gameId }) {
  const response = await fetch(`${BASE_URL}/library/add`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      gameId,
    }),
  });

  if (!response.ok) {
    throw new Error("Failed to add game to library");
  }

  return response.json();
}