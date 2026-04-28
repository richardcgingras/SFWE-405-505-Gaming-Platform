import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getLibraryById } from "../../services/GameLibrary.js";
import Navbar from "../../components/Navbar/Navbar.jsx";
import "./Library.css";

export default function Library() {
  const [library, setLibrary] = useState([]);
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

  useEffect(() => {
    const fetchLibrary = async () => {
      try {
        setLoading(true);
        const data = await getLibraryById(101);
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

  return (
    <>
      {/* Header and nav bar */}
      <div className="page">
        <div className="bg-glow" />

        <Navbar />

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
