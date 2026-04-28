import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { removeGameFromCart, getGames } from "../../services/ShoppingCart.js";
import Navbar from "../../components/Navbar/Navbar.jsx";
import "./Cart.css";

export default function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const navigate = useNavigate();
  const username = localStorage.getItem("username") || "";

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    setIsLoggedIn(false);
    navigate("/login");
  };

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
    // Redirect to checkout page or API call
    window.location.href = "/checkout";
  };

  const totalGames = cartItems.length;
  const totalSizeInGB = cartItems.reduce(
    (sum, game) => sum + (game.size || 0),
    0,
  );

  if (loading) {
    return (
      <div className="cart-container">
        <h2>Shopping Cart</h2>
        <p>Loading cart items...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="cart-container">
        <h2>Shopping Cart</h2>
        <p className="error-message">{error}</p>
        <button onClick={fetchCartContents}>Try Again</button>
      </div>
    );
  }

  return (
    //Header and nav bar
    <div className="page">
      <div className="bg-glow" />

      <Navbar />
      <div className="cart-container">
        <h2>
          Shopping Cart ({totalGames} {totalGames === 1 ? "game" : "games"})
        </h2>

        {cartItems.length === 0 ? (
          <p>Your cart is empty. Add some games!</p>
        ) : (
          <>
            <div className="cart-items">
              {cartItems.map((game) => (
                <div key={game.id} className="cart-item">
                  <div className="item-main-info">
                    <span className="game-name">{game.name}</span>
                    <div className="item-details">
                      {/* <div className="release-date">
                                            Release Date: {game.releaseDate ? new Date(game.releaseDate.getTime()).toLocaleDateString() : 'N/A'}
                                        </div> */}
                      <div className="file-size">
                        Size: {(game.size || 0).toFixed(2)} GB
                      </div>
                      {game.ageRating && (
                        <div className="age-rating">
                          Age Rating: {game.ageRating}
                        </div>
                      )}
                    </div>
                  </div>

                  {game.category && game.category.length > 0 && (
                    <div className="item-category">
                      Categories:
                      <ul className="category-list">
                        {game.category.map((cat, idx) => (
                          <li key={idx}>{cat.name}</li>
                        ))}
                      </ul>
                    </div>
                  )}

                  {game.system && game.system.length > 0 && (
                    <div className="item-system">
                      Compatible Systems:
                      <ul className="system-list">
                        {game.system.map((sys, idx) => (
                          <li key={idx}>{sys.name}</li>
                        ))}
                      </ul>
                    </div>
                  )}

                  {game.publisher && (
                    <div className="item-publisher">
                      Publisher:{" "}
                      {game.publisher.name || game.publisher.companyName}
                    </div>
                  )}

                  {game.reviews && game.reviews.length > 0 && (
                    <div className="item-reviews">
                      Reviews ({game.reviews.reduce((sum, r) => sum + r, 0)}):
                      {game.reviews.map((r, idx) => (
                        <span key={idx} title={`Score: ${r}`}>
                          ★
                        </span>
                      ))}
                    </div>
                  )}

                  <div className="item-files">
                    Files:{" "}
                    {game.files && game.files.length > 0
                      ? game.files.join(", ")
                      : "No files yet"}
                  </div>

                  <div className="item-footer">
                    <span className="item-price">
                      Price: ${game.price.toFixed(2)}
                    </span>
                    <button
                      className="remove-btn"
                      onClick={() => removeItem(game.id)}
                      title="Remove from cart"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <div className="cart-summary">
              <div className="summary-row">
                <span>Total Games:</span>
                <span>{totalGames}</span>
              </div>
              <div className="summary-row">
                <span>Total Size:</span>
                <span>{totalSizeInGB.toFixed(2)} GB</span>
              </div>
            </div>

            <button
              className="checkout-btn"
              onClick={handleCheckout}
              title="Proceed to checkout"
            >
              Checkout →
            </button>
          </>
        )}
      </div>

      {/*All the boring stuff at bottom of the page*/}
      <footer className="footer">
        <div className="footer-logo"> goodGamers</div>
        <p className="footer-copy">
          {" "}
          2026 goodGamers Inc. SFWE 405/505. The University of Arizona
        </p>
        <div className="footer-links">
          <a href="#">Privacy</a>
          <a href="#">Terms</a>
          <a href="#">Support</a>
        </div>
      </footer>
    </div>
  );
}
