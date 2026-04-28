import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function VideoGames() {
    const [games, setGames] = useState([]);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");

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
        <main className="main" style={{ paddingTop: '40px' }}>
            <section className="section">
                <div className="section-header">
                    <h2 className="section-title">All Games</h2>
                    <input
                        type="text"
                        placeholder="Search games..."
                        className="form-input"
                        style={{ width: '300px' }}
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>

                {loading && <div className="section-status">LOADING...</div>}

                {!loading && filteredGames.length === 0 && (
                    <div className="hero-sub">No games found.</div>
                )}

                {!loading && filteredGames.length > 0 && (
                    <div className="games-grid">
                        {filteredGames.map((game, index) => (
                            <div
                                key={game.id}
                                className="game-card"
                                style={{ animationDelay: `${index * 0.05}s` }}
                            >
                                <div className="game-card-info" style={{ flexDirection: 'column', alignItems: 'flex-start' }}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%', marginBottom: '10px' }}>
                                        <h3 className="game-card-title">{game.name}</h3>
                                        <span className="game-price">${game.price}</span>
                                    </div>
                                    <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                                        <div>{game.category?.map(c => c.type).join(", ")}</div>
                                        <div style={{ marginTop: '4px', opacity: 0.7 }}>
                                            Released: {new Date(game.releaseDate).toLocaleDateString()}
                                        </div>
                                    </div>
                                    <button className="btn btn-red" style={{ width: '100%', marginTop: '20px', padding: '8px' }}>
                                        View Details
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </section>
        </main>
    );
}