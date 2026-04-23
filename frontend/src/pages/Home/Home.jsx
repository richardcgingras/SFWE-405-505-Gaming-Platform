import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { getAllVideoGames } from "../../services/VideoGame.js";
import "./App.css";

const getToken = () => localStorage.getItem("token") || "";

const getCurrentUser = () => {
  const token = getToken();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return {
      id: payload.sub,
      username: localStorage.getItem("username") || "User",
    };
  } catch {
    return null;
  }
};

export default function Home() {
  const [games, setGames] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    setUser(getCurrentUser());
  }, []);

  useEffect(() => {
    const fetchGames = async () => {
      try {
        const data = await getAllVideoGames();
        setGames(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchGames();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    setUser(null);
    navigate("/");
  };

  return (
    <div className="page">
      <div className="bg-glow" />
      <nav className="nav">
        <div className="nav-logo">
          <Link to="/">
            <span className="logo-icon"></span>
            <span className="logo-text">good<span>Gamers</span></span>
          </Link>
        </div>
        <ul className="nav-links">
          <li><a href="/store">Store</a></li>
          <li><a href="/library">Library</a></li>
          <li><Link to="/community">Community</Link></li>
          <li><a href="/news">News</a></li>
        </ul>
        <div className="nav-actions">
          {user ? (
            <div className="nav-user">
              <div className="nav-avatar">
                {user.username?.[0]?.toUpperCase()}
              </div>
              <span className="nav-username">{user.username}</span>
              <button className="btn btn-ghost" onClick={handleLogout}>
                Log Out
              </button>
            </div>
          ) : (
            <>
              <Link to="/login" className="btn btn-ghost">Log In</Link>
              <Link to="/signup" className="btn btn-red">Sign Up</Link>
            </>
          )}
        </div>
      </nav>

      <section className="hero">
        <div className="hero-content">
          <div className="hero-tag">14.2M Players Online Now</div>
          <h1 className="hero-title">
            YOUR UNIVERSE.<br />
            <span className="hero-title-accent">YOUR RULES.</span>
          </h1>
          <p className="hero-sub">
            The ultimate gaming platform — play, buy, and compete with friends
            across thousands of worlds.
          </p>
          <div className="hero-cta">
            <button className="btn btn-red">Browse Store →</button>
            <button className="btn btn-ghost">Watch Trailer ▶</button>
          </div>
        </div>
      </section>

      <div className="stats-bar">
        {[
          { label: "Games", val: "12,400+" },
          { label: "Players", val: "14.2M" },
          { label: "Daily Deals", val: "340" },
          { label: "Countries", val: "180+" },
        ].map((s) => (
          <div key={s.label} className="stat">
            <span className="stat-val">{s.val}</span>
            <span className="stat-label">{s.label}</span>
          </div>
        ))}
      </div>

      <main className="main">
        <section className="section">
          <div className="section-header">
            <h2 className="section-title">Featured Games</h2>
            <Link to="/games" className="section-link">View All →</Link>
          </div>

          {loading && <p className="section-status">Loading games...</p>}
          {error && <p className="section-status error">Error: {error}</p>}

          {!loading && !error && (
            <div className="games-grid">
              {games.map((game, i) => (
                <div
                  key={game.id}
                  className="game-card"
                  style={{ animationDelay: `${i * 0.1}s` }}
                >
                  <div className="game-card-info">
                    <h3 className="game-card-title">{game.title}</h3>
                    <span className="game-price">
                      {game.price === 0 ? "FREE" : `$${game.price}`}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>

      <footer className="footer">
        <div className="footer-logo">goodGamers</div>
        <p className="footer-copy">
          2026 goodGamers Inc. SFWE 405. The University of Arizona
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