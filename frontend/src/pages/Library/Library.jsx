import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getLibraryById } from "../../services/GameLibrary.js";
import "./Library.css";

// Add authentication context or use localStorage for auth state
const isAuthenticated = !!localStorage.getItem("token"); // Example check
const getId = () => localStorage.getItem("userId") || "";

export default function Library() {
  const [library, setLibrary] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Initialize navigator
  const userMenu = (
    <>
      <div className="nav-avatar"></div>
      <Link to="/cart" className="btn btn-ghost">
        Cart
      </Link>
      <Link to="/list" className="btn btn-ghost">
        Wish List
      </Link>
      <Link to="/account" className="btn btn-red">
        Account
      </Link>
    </>
  );

  useEffect(() => {
    const fetchLibrary = async () => {
      try {
        setLoading(true);
        const data = await getLibraryById(getId());
        setLibrary(data || []); // Handle empty response safely
        setError(null);
      } catch (err) {
        setLibrary([]);
        setError(null);
      } finally {
        setLoading(false);
      }
    };

    fetchLibrary();
  }, []);

  if (loading) {
    return <div className="library-loading">Loading your library...</div>;
  }

  if (error) {
    return <div className="library-error">Error: {error}</div>;
  }

  // console.log(library);
  // console.log("Library Qty: " + library.owner.gameLibrary.length);

  return (
    <>
      {/* Header and nav bar */}
      <div className="page">
        <div className="bg-glow" />

        <nav className="nav">
          <div className="nav-logo">
            <span className="logo-icon"></span>
            <span className="logo-text">
              good<span>Gamers</span>
            </span>
          </div>
          <ul className="nav-links">
            <li>
              <a href="#">Store</a>
            </li>
            <li>
              <a href="Library">Library</a>
            </li>
            <li>
              <Link to="/community">Community</Link>
            </li>
            <li>
              <a href="#">News</a>
            </li>
          </ul>
          <div className="nav-actions">
            {!isAuthenticated ? (
              <>
                <Link to="/login" className="btn btn-ghost">
                  Log In
                </Link>
                <Link to="/signup" className="btn btn-red">
                  Sign Up
                </Link>
              </>
            ) : (
              userMenu
            )}
          </div>
        </nav>

        <div className="library-container">
          {/* ... existing loading/error checks ... */}

          {error && <div className="library-error">Error: {error}</div>}

          {library.length === 0 ? (
            <p className="library-empty-text">Your library is empty.</p>
          ) : (
            <div className="library-page">
              <h1>Your Game Library</h1>
              {library.owner.gameLibrary.length === 0 ? (
                <p className="library-empty-text">Your library is empty.</p>
              ) : (
                <ul className="library-grid">
                  {library.owner.gameLibrary.map((game) => (
                    <li
                      key={game.id}
                      className="library-game-card"
                      onClick={() => navigate(`/Download?id=${game.id}`)} // Added click handler
                    >
                      <h3>{game.name}</h3>
                      <span className="system">
                        {game.system || "Error"}
                        <br></br>
                      </span>
                      <span className="size">
                        {game.size || "Error"}GB<br></br>
                      </span>
                      <span className="rating">
                        {game.ageRating || "Error"}
                        <br></br>
                      </span>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>

        {/* All the boring stuff at bottom of the page */}
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
    </>
  );
}
