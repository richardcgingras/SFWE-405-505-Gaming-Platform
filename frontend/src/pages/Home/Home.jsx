import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { getAllVideoGames } from "../../services/VideoGame.js";

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
    const currentUser = getCurrentUser();
    if (!currentUser) {
      navigate("/");
    } else {
      setUser(currentUser);
    }
  }, [navigate]);

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
    if (getToken()) {
      fetchGames();
    }
  }, []);

  return (
    <main className="main" style={{ paddingTop: '40px' }}>
      <header className="dashboard-header" style={{ marginBottom: '40px' }}>
        <h1 className="hero-title" style={{ fontSize: '3rem', marginBottom: '10px' }}>
          Welcome back, <span className="hero-title-accent">{user?.username}</span>
        </h1>
        <p className="hero-sub">What are we playing today?</p>
      </header>

      <section className="section">
        <div className="section-header">
          <h2 className="section-title">Jump Back In</h2>
          <Link to="/games" className="section-link">Browse All Games →</Link>
        </div>

        {loading && <p className="section-status">Loading your universe...</p>}
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
  );
}