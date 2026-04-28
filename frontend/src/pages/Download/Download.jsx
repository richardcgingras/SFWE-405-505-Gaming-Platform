import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getVideoGameById, getFile } from "../../services/VideoGame.js";
import "./Download.css";

// Add authentication context or use localStorage for auth state
const isAuthenticated = !!localStorage.getItem("token"); // Example check

export default function Download() {
  const [game, setGame] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
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
    const fetchGame = async () => {
      try {
        setLoading(true);
        const getQueryParams = () =>
          new URLSearchParams(window.location.search);
        const gameId = parseInt(getQueryParams().get("id")) || null;
        const data = await getVideoGameById(gameId);
        console.log("API response: ", data);
        setGame(data || []); // Handle empty response safely
        setError(null);
        console.log("Game data", game);
        console.log(game.id);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchGame();
  }, []);

  if (loading) {
    return (
      <div className="download-page">
        <h1>Download Files - {game.name}</h1>
        <p>Loading the Game Files...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="download-page">
        <h1>Error</h1>
        <p className="error-message">{error}</p>
        <button onClick={() => navigate("/")}>Go Back</button>
      </div>
    );
  }

  const handleFileDownload = async (file) => {
    if (!game.id || !game.files.length) return;

    try {
      setError(null);
      const response = await getFile(game.id, file);
      // Create download for Blob response
      if (response instanceof Blob) {
        const url = URL.createObjectURL(response);
        const a = document.createElement("a");
        a.href = url;
        a.download = `${game.name}-${file}`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
      } else if (typeof response === "string") {
        // Handle JSON/string responses
        const blob = new Blob([response]);
        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `${game.name}-${file}`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
      } else {
        alert("Failed to download the file.");
      }
    } catch (err) {
      console.error("Download failed:", err);
      setError(err.message);
    }
  };

  return (
    //Header and nav bar
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
      <div className="download-page">
        <h1>Download Files - {game.name}</h1>

        <div className="file-list">
          {game.files.length === 0 ? (
            <p>Your game contains no files.</p>
          ) : (
            <ul className="library-grid">
              {game.files.map((file) => (
                <button
                  key={file}
                  onClick={() => handleFileDownload(file)}
                  className="download-file-btn"
                >
                  📁 {file}
                </button>
              ))}
            </ul>
          )}
        </div>
      </div>

      {/*All the boring stuff at bottom of the page*/}
      <footer className="footer">
        <div className="footer-logo">goodGamers</div>
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
