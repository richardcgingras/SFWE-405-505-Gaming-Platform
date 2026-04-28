import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getAllVideoGames } from "../../services/VideoGame.js";
import "./Store.css";

export default function Store() {
  const [games, setGames] = useState([]);
  const [search, setSearch] = useState("");
  const [selectedGame, setSelectedGame] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const loadGames = async () => {
      try {
        const data = await getAllVideoGames();
        setGames(data || []);
      } catch (err) {
        console.error("Error loading store games:", err);
        setMessage("Unable to load store games.");
      } finally {
        setLoading(false);
      }
    };

    loadGames();
  }, []);

  const filteredGames = games.filter((game) =>
    (game.name || game.title || "")
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  const formatPrice = (price) => {
    if (price === 0) return "FREE";
    if (price == null) return "Price unavailable";
    return `$${Number(price).toFixed(2)}`;
  };

  const getCategories = (game) => {
    if (!game.category || game.category.length === 0) {
      return "No category listed";
    }

    return game.category
      .map((cat) => cat.type || cat.name || cat)
      .join(", ");
  };

  return (
    <main className="main">
      <section className="section">
        <div className="store-header">
          <div>
            <h2 className="section-title">Store</h2>
            <p className="store-subtitle">
              Browse available games, compare prices, and view game details.
            </p>
          </div>

         
            <input
              type="text"
              placeholder="Search store..."
              className="store-search"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
   

        {loading && <div className="store-loading">LOADING STORE...</div>}

        {!loading && message && (
          <div className="store-empty">{message}</div>
        )}

        {!loading && !message && filteredGames.length === 0 && (
          <div className="store-empty">No games found.</div>
        )}

        {!loading && !message && filteredGames.length > 0 && (
          <div className="store-grid">
            {filteredGames.map((game, index) => (
              <div
                key={game.id}
                className="store-card"
                style={{ animationDelay: `${index * 0.05}s` }}
              >
                <div className="store-card-content">
                  <div>
                    <h3 className="store-game-title">
                      {game.name || game.title}
                    </h3>

                    <div className="store-price">
                      {formatPrice(game.price)}
                    </div>

                    <div className="store-meta">
                      {getCategories(game)}
                    </div>

                    {game.releaseDate && (
                      <div className="store-meta">
                        Release: {new Date(game.releaseDate).toLocaleDateString()}
                      </div>
                    )}

                    {game.ageRating && (
                      <div className="store-meta">
                        Rating: {game.ageRating}
                      </div>
                    )}
                  </div>

                  <button
                    className="btn btn-red store-action"
                    onClick={() => setSelectedGame(game)}
                  >
                    View Details
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {selectedGame && (
          <div className="store-detail-card">
            <div>
              <h3>{selectedGame.name || selectedGame.title}</h3>
              <p>{selectedGame.description || "No description available."}</p>
              <p><strong>Price:</strong> {formatPrice(selectedGame.price)}</p>
              <p><strong>Categories:</strong> {getCategories(selectedGame)}</p>
              {selectedGame.size && (
                <p><strong>Size:</strong> {selectedGame.size} GB</p>
              )}
            </div>

            <button
              className="btn btn-ghost"
              onClick={() => setSelectedGame(null)}
            >
              Close
            </button>
          </div>
        )}
      </section>
    </main>
  );
}