import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getLibraryById } from "../../services/GameLibrary.js";

export default function Library() {
    const [library, setLibrary] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLibrary = async () => {
            try {
                setLoading(true);
                const data = await getLibraryById(101);
                setLibrary(data);
                setError(null);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchLibrary();
    }, []);

    if (loading) {
        return <div className="section-status">Loading your library...</div>;
    }

    if (error) {
        return <div className="section-status error">Error: {error}</div>;
    }

    return (
        <main className="main" style={{ paddingTop: '40px' }}>
            <div className="section-header">
                <h2 className="section-title">Your Game Library</h2>
            </div>
            {!library || !library.owner || library.owner.gameLibrary.length === 0 ? (
                <p className="hero-sub">Your library is empty.</p>
            ) : (
                <div className="games-grid">
                    {library.owner.gameLibrary.map((game) => (
                        <div
                            key={game.id}
                            className="game-card"
                            onClick={() => navigate(`/Download?id=${game.id}`)}
                            style={{ cursor: 'pointer' }}
                        >
                            <div className="game-card-info" style={{ flexDirection: 'column', alignItems: 'flex-start' }}>
                                <h3 className="game-card-title">{game.name}</h3>
                                <div style={{ display: 'flex', gap: '10px', marginTop: '5px', fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                                    <span>{game.system}</span>
                                    <span>•</span>
                                    <span>{game.size}GB</span>
                                    <span>•</span>
                                    <span>{game.ageRating}</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </main>
    );
}
