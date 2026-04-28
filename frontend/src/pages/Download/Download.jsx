import { useState, useEffect } from "react";
import { getVideoGameById, getFile } from "../../services/VideoGame.js"

export default function Download() {
    const [game, setGame] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchGame = async () => {
            try {
                setLoading(true);
                const getQueryParams = () => new URLSearchParams(window.location.search);
                const gameId = parseInt(getQueryParams().get('id')) || null;
                const data = await getVideoGameById(gameId);
                setGame(data);
                setError(null);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchGame();
    }, []);

    const handleFileDownload = async (file) => {
        if (!game?.id) return;

        try {
            setError(null);
            const response = await getFile(game.id, file);
            if (response instanceof Blob) {
                const url = URL.createObjectURL(response);
                const a = document.createElement('a');
                a.href = url;
                a.download = `${game.name}-${file}`;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
            } else if (typeof response === 'string') {
                const blob = new Blob([response]);
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
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

    if (loading) {
        return <div className="section-status">Loading Game Files...</div>;
    }

    if (error) {
        return <div className="section-status error">Error: {error}</div>;
    }

    return (
        <main className="main" style={{ paddingTop: '40px' }}>
            <div className="section-header">
                <h2 className="section-title">Download Files - {game?.name}</h2>
            </div>

            {!game || !game.files || game.files.length === 0 ? (
                <p className="hero-sub">This game contains no files.</p>
            ) : (
                <div className="games-grid">
                    {game.files.map((file) => (
                        <div
                            key={file}
                            className="game-card"
                            onClick={() => handleFileDownload(file)}
                            style={{ cursor: 'pointer' }}
                        >
                            <div className="game-card-info">
                                <h3 className="game-card-title">📁 {file}</h3>
                                <button className="btn btn-red" style={{ padding: '6px 12px', fontSize: '0.75rem' }}>
                                    Download
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </main>
    );
}
