import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { removeGameFromCart, getGames } from "../../services/ShoppingCart.js";
import "./Cart.css";

export default function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [toast, setToast] = useState("");
  const [purchasing, setPurchasing] = useState(false);
  const [purchased, setPurchased] = useState(false);
  const navigate = useNavigate();

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(""), 3500);
  };

  const fetchCartContents = async () => {
    try {
      setLoading(true);
      const contents = await getGames();
      setCartItems(Array.isArray(contents) ? contents : []);
      setError(null);
    } catch (err) {
      console.error("Failed to load cart:", err);
      setError(err.message || "Could not load cart items");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCartContents();
  }, []);

  const removeItem = async (gameId) => {
    try {
      await removeGameFromCart(gameId);
      fetchCartContents();
    } catch (err) {
      showToast(`Error removing game: ${err.message}`);
    }
  };

  // Demo purchase: add every cart game to the user's library, then clear cart
  const handleDemoPurchase = async () => {
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    if (!userId || !token) {
      showToast("Please log in first.");
      return;
    }
    setPurchasing(true);
    try {
      await Promise.all(
        cartItems.map((game) =>
          fetch(`/api/gamelibrary/user/${userId}/games/${game.id}`, {
            method: "POST",
            headers: { Authorization: `Bearer ${token}` },
          })
        )
      );
      // Remove all items from cart
      await Promise.all(cartItems.map((game) => removeGameFromCart(game.id).catch(() => {})));
      setPurchased(true);
    } catch (err) {
      showToast(`Purchase failed: ${err.message}`);
    } finally {
      setPurchasing(false);
    }
  };

  const totalPrice = cartItems.reduce((sum, g) => sum + (g.price || 0), 0);
  const totalGames = cartItems.length;

  // ── Success screen ──────────────────────────────────────────────────────────
  if (purchased) {
    return (
      <main className="main" style={{ paddingTop: "40px" }}>
        <div className="cart-success">
          <div className="cart-success-icon">✓</div>
          <h2 className="cart-success-title">Purchase Complete!</h2>
          <p className="cart-success-sub">
            All {totalGames} {totalGames === 1 ? "game has" : "games have"} been added to your library.
          </p>
          <div style={{ display: "flex", gap: "12px", justifyContent: "center", marginTop: "24px" }}>
            <button className="btn btn-red" onClick={() => navigate("/library")}>
              Go to Library
            </button>
            <button className="btn btn-ghost" onClick={() => navigate("/store")}>
              Back to Store
            </button>
          </div>
        </div>
      </main>
    );
  }

  // ── Loading / Error ─────────────────────────────────────────────────────────
  if (loading) {
    return (
      <main className="main" style={{ paddingTop: "40px" }}>
        <p className="hero-sub">Loading cart…</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="main" style={{ paddingTop: "40px" }}>
        <p style={{ color: "var(--red)" }}>{error}</p>
        <button className="btn btn-ghost" onClick={fetchCartContents} style={{ marginTop: "12px" }}>
          Try Again
        </button>
      </main>
    );
  }

  // ── Main cart ───────────────────────────────────────────────────────────────
  return (
    <main className="main" style={{ paddingTop: "40px" }}>
      {toast && <div className="lib-toast">{toast}</div>}

      <section className="section">
        <div className="section-header">
          <h2 className="section-title">
            Shopping Cart ({totalGames} {totalGames === 1 ? "game" : "games"})
          </h2>
        </div>

        {cartItems.length === 0 ? (
          <div style={{ textAlign: "center", padding: "60px 0" }}>
            <p className="hero-sub" style={{ marginBottom: "20px" }}>Your cart is empty.</p>
            <a href="/store" className="btn btn-red">Browse Store</a>
          </div>
        ) : (
          <>
            <div className="cart-items">
              {cartItems.map((game) => (
                <div key={game.id} className="cart-game-row">
                  <div className="cart-game-info">
                    <h3 className="game-card-title">{game.name}</h3>
                    <div style={{ marginTop: "6px", fontSize: "0.82rem", color: "var(--text-secondary)" }}>
                      {game.ageRating && <span style={{ marginRight: "12px" }}>Rating: {game.ageRating}</span>}
                      {game.size != null && <span>{game.size} GB</span>}
                    </div>
                  </div>
                  <div className="cart-game-right">
                    <span className="game-price" style={{ fontSize: "1.2rem" }}>
                      {game.price === 0 ? "FREE" : `$${Number(game.price).toFixed(2)}`}
                    </span>
                    <button
                      className="btn btn-ghost"
                      style={{ color: "var(--red)", fontSize: "0.8rem", padding: "6px 12px" }}
                      onClick={() => removeItem(game.id)}
                    >
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Summary */}
            <div className="cart-summary-box">
              <div className="cart-summary-row">
                <span>Games</span>
                <span>{totalGames}</span>
              </div>
              <div className="cart-summary-row cart-summary-total">
                <span>Total</span>
                <span className="cart-total-price">${totalPrice.toFixed(2)}</span>
              </div>

              <button
                className="btn btn-red btn-full"
                style={{ fontSize: "1rem", padding: "14px", marginTop: "20px" }}
                onClick={handleDemoPurchase}
                disabled={purchasing}
              >
                {purchasing ? "Processing…" : "✓ Complete Purchase (Demo)"}
              </button>
              <p style={{ textAlign: "center", fontSize: "0.78rem", color: "var(--text-muted)", marginTop: "10px" }}>
                Demo mode — no payment info required
              </p>
            </div>
          </>
        )}
      </section>
    </main>
  );
}
