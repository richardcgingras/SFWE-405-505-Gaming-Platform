import { useState, useEffect } from "react";
import { getWishlistGames, removeGameFromWishlist, getWishlistTotal } from "../../services/Wishlist.js";
import { addGameToCart } from "../../services/ShoppingCart.js";
import "./wishlist.css";

export default function Wishlist() {
  const [wishlistItems, setWishlistItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [totalPrice, setTotalPrice] = useState(0);

  const fetchWishlistContents = async () => {
    try {
      setLoading(true);
      const contents = await getWishlistGames();
      setWishlistItems(contents || []);
      
      const totalData = await getWishlistTotal();
      setTotalPrice(parseFloat(totalData.total) || 0);
      
      setError(null);
    } catch (err) {
      console.error("Failed to load wishlist:", err);
      setError(err.message || "Could not load wishlist items");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWishlistContents();
  }, []);

  const removeItem = async (gameId) => {
    try {
      await removeGameFromWishlist(gameId);
      fetchWishlistContents();
    } catch (err) {
      console.error("Failed to remove item:", err);
      alert(`Error removing game: ${err.message}`);
    }
  };

  const moveToCart = async (gameId) => {
    try {
      const response = await addGameToCart(gameId);
      if (response.Status === "success") {
        await removeGameFromWishlist(gameId);
        fetchWishlistContents();
        alert("Game added to cart!");
      } else {
        alert(`Failed to add to cart: ${response.Status || "Unknown error"}`);
      }
    } catch (err) {
      console.error("Failed to move item to cart:", err);
      alert(`Error moving game to cart: ${err.message}`);
    }
  };

  const totalGames = wishlistItems.length;

  if (loading) {
    return (
      <main className="main" style={{ paddingTop: '40px' }}>
        <div className="section-status">
          <p>Loading your wishlist...</p>
        </div>
      </main>
    );
  }

  return (
    <main className="main" style={{ paddingTop: '40px' }}>
      <section className="section">
        <div className="section-header">
          <h2 className="section-title">
            My Wishlist ({totalGames} {totalGames === 1 ? "game" : "games"})
          </h2>
        </div>

        {error && (
          <div className="section-status error">
            <p>{error}</p>
            <button className="btn btn-ghost" onClick={fetchWishlistContents}>Try Again</button>
          </div>
        )}

        {wishlistItems.length === 0 ? (
          <div className="hero-sub">Your wishlist is empty. Explore the store to find your next adventure!</div>
        ) : (
          <>
            <div className="wishlist-items">
              {wishlistItems.map((game) => (
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
                    <button 
                      className="btn btn-ghost" 
                      onClick={() => moveToCart(game.id)}
                      style={{ border: '1px solid var(--blue-border)' }}
                    >
                      Move to Cart
                    </button>
                    <button className="btn btn-red" onClick={() => window.location.href = `/games/${game.id}`}>
                      View Details
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <div className="wishlist-summary" style={{ background: 'var(--blue-card)', padding: '24px', borderRadius: '8px', border: '1px solid var(--blue-border)', marginTop: '40px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                <span style={{ color: 'var(--text-secondary)' }}>Total Games:</span>
                <span style={{ fontWeight: 700 }}>{totalGames}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '24px' }}>
                <span style={{ color: 'var(--text-secondary)' }}>Estimated Total:</span>
                <span style={{ fontWeight: 700, color: 'var(--red)', fontSize: '1.2rem' }}>${totalPrice.toFixed(2)}</span>
              </div>
              <button
                className="btn btn-red btn-full"
                onClick={() => window.location.href = '/store'}
                style={{ fontSize: '1.1rem', padding: '16px' }}
              >
                Continue Shopping
              </button>
            </div>
          </>
        )}
      </section>
    </main>
  );
}
