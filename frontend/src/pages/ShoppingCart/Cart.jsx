import { useState, useEffect } from "react";
import { removeGameFromCart, getGames } from "../../services/ShoppingCart.js";
import "./Cart.css";

export default function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchCartContents = async () => {
    try {
      setLoading(true);
      const contents = await getGames();
      setCartItems(contents || []);
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
      console.error("Failed to remove item:", err);
      alert(`Error removing game ${gameId}: ${err.message}`);
    }
  };

  const handleCheckout = () => {
    if (cartItems.length === 0) {
      alert("Your cart is empty!");
      return;
    }
    console.log("Proceeding to checkout with", cartItems.length, "items");
    window.location.href = "/checkout";
  };

  const totalGames = cartItems.length;
  const totalSizeInGB = cartItems.reduce(
    (sum, game) => sum + (game.size || 0),
    0,
  );

  if (loading) {
    return (
      <div className="section-status">
        <p>Loading cart items...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="section-status error">
        <p>{error}</p>
        <button className="btn btn-ghost" onClick={fetchCartContents}>Try Again</button>
      </div>
    );
  }

  return (
    <main className="main" style={{ paddingTop: '40px' }}>
      <section className="section">
        <div className="section-header">
          <h2 className="section-title">
            Shopping Cart ({totalGames} {totalGames === 1 ? "game" : "games"})
          </h2>
        </div>

        {cartItems.length === 0 ? (
          <p className="hero-sub">Your cart is empty. Add some games!</p>
        ) : (
          <>
            <div className="cart-items">
              {cartItems.map((game) => (
                <div key={game.id} className="game-card" style={{ marginBottom: '20px', padding: '24px' }}>
                  <div className="item-main-info" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                    <div>
                      <h3 className="game-card-title" style={{ fontSize: '1.25rem' }}>{game.name}</h3>
                      <div className="game-meta" style={{ marginTop: '8px' }}>
                        <span style={{ color: 'var(--text-secondary)' }}>Size: {(game.size || 0).toFixed(2)} GB</span>
                        {game.ageRating && <span style={{ marginLeft: '16px', color: 'var(--text-secondary)' }}>Rating: {game.ageRating}</span>}
                      </div>
                    </div>
                    <span className="game-price" style={{ fontSize: '1.25rem' }}>
                      ${game.price.toFixed(2)}
                    </span>
                  </div>

                  <div className="item-footer" style={{ marginTop: '24px', display: 'flex', justifyContent: 'flex-end', gap: '12px' }}>
                    <button
                      className="btn btn-ghost"
                      onClick={() => removeItem(game.id)}
                      style={{ color: 'var(--red)' }}
                    >
                      Remove
                    </button>
                    <button className="btn btn-red" onClick={() => window.location.href = `/games/${game.id}`}>
                      View Details
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <div className="cart-summary" style={{ background: 'var(--blue-card)', padding: '24px', borderRadius: '8px', border: '1px solid var(--blue-border)', marginTop: '40px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                <span style={{ color: 'var(--text-secondary)' }}>Total Games:</span>
                <span style={{ fontWeight: 700 }}>{totalGames}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '24px' }}>
                <span style={{ color: 'var(--text-secondary)' }}>Total Download Size:</span>
                <span style={{ fontWeight: 700 }}>{totalSizeInGB.toFixed(2)} GB</span>
              </div>
              <button
                className="btn btn-red btn-full"
                onClick={handleCheckout}
                style={{ fontSize: '1.1rem', padding: '16px' }}
              >
                Checkout Now →
              </button>
            </div>
          </>
        )}
      </section>
    </main>
  );
}
