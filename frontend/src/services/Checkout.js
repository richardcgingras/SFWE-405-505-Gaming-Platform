const BASE_URL = "http://localhost:8080/api";

export async function purchaseGame({ gameId }) {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/orders`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify({
      game: { id: gameId },
      destinationAccount: { id: JSON.parse(atob(token.split('.')[1])).sub },
      purchaseTimestamp: new Date(),
      paymentProcessed: true
    }),
  });

  if (!response.ok) {
    throw new Error("Purchase failed");
  }

  return response.json();
}