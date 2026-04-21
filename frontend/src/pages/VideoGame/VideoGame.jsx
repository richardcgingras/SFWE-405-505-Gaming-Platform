import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./VideoGame.css";

export default function VideoGames() {
    const [games, setGames] = useState([]);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");

        // 🚨 If not logged in → redirect
        if (!token) {
            navigate("/login");
            return;
        }

        fetch("http://localhost:8080/api/video-games", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        })
            .then(res => {
                if (res.status === 401) {
                    // token invalid or expired
                    localStorage.removeItem("token");
                    navigate("/login");
                    return null;
                }
                return res.json();
            })
            .then(data => {
                if (data) setGames(data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Error fetching games:", err);
                setLoading(false);
            });

    }, [navigate]);

    const filteredGames = games.filter(game =>
        game.name.toLowerCase().includes(search.toLowerCase())
    );

    return (
        <div className="page">
            {/* BACKGROUND */}
            <div className="bg-glow"></div>

            {/* NAV */}
            <nav className="nav">
                <div className="nav-logo">
                    <span className="logo-icon">🎮</span>
                    <span className="logo-text">
            GOOD<span>GAMERS</span>
          </span>
                </div>

                <ul className="nav-links">
                    <li><a href="/">Home</a></li>
                    <li><a href="/games">Games</a></li>
                </ul>

                <div className="nav-actions">
                    <button
                        className="btn btn-ghost"
                        onClick={() => {
                            localStorage.removeItem("token");
                            navigate("/login");
                        }}
                    >
                        Logout
                    </button>
                </div>
            </nav>

            {/* MAIN */}
            <main className="main">
                <section className="section">

                    {/* HEADER */}
                    <div className="games-header">
                        <h2 className="section-title">All Games</h2>

                        <input
                            type="text"
                            placeholder="Search games..."
                            className="games-search"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />
                    </div>

                    {/* STATES */}
                    {loading && <div className="games-loading">LOADING...</div>}

                    {!loading && filteredGames.length === 0 && (
                        <div className="games-empty">No games found.</div>
                    )}

                    {/* GRID */}
                    {!loading && filteredGames.length > 0 && (
                        <div className="games-grid">
                            {filteredGames.map((game, index) => (
                                <div
                                    key={game.id}
                                    className="game-card"
                                    style={{ animationDelay: `${index * 0.05}s` }}
                                >
                                    <div className="game-card-info">

                                        <div className="game-meta">
                                            <div className="game-card-title">
                                                {game.name}
                                            </div>

                                            <div className="game-price">
                                                ${game.price}
                                            </div>

                                            <div className="game-category">
                                                {game.category?.map(c => c.type).join(", ")}
                                            </div>

                                            <div className="game-date">
                                                {new Date(game.releaseDate).toLocaleDateString()}
                                            </div>
                                        </div>

                                        <button className="btn btn-red game-action">
                                            View
                                        </button>

                                    </div>
                                </div>
                            ))}
                        </div>
                    )}

                </section>
            </main>

            {/* FOOTER */}
            <footer className="footer">
                <div className="footer-logo">GOODGAMERS</div>
                <div className="footer-copy">© 2026 Gaming Platform</div>
            </footer>
        </div>
    );
}