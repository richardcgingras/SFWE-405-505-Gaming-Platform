import "./Upload.css";
import { useState, useEffect } from "react";
import { uploadFile, getVideoGameById } from "../../services/VideoGame.js";

export default function Upload(){
    const [devId, setDevId] = useState(null);
    const [gameId, setGameId] = useState(null);
    const [gameData, setGameData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitted, setSubmitted] = useState(false);
    const [selectedFile, setSelectedFile] = useState(null);

    useEffect(() => {
        const fetchDev = async () => {
            try {
                setLoading(true);
                const getQueryParams = () => new URLSearchParams(window.location.search);
                const devIdParam = parseInt(getQueryParams().get("devId")) || null;
                const gameIdParam = parseInt(getQueryParams().get("gameId")) || null;
                
                if (gameIdParam) {
                    const data = await getVideoGameById(gameIdParam);
                    setGameData(data);
                }
                
                setDevId(devIdParam);
                setGameId(gameIdParam);
                setError(null);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchDev();
    }, []);

    const onFileChange = (event) => {
		setSelectedFile(event.target.files[0]);
	};

    const onFileUpload = async (event) => {
        event.preventDefault();
        if (!selectedFile || !gameId) return;

        setLoading(true);
        setSubmitted(false);
        
        try {
            await uploadFile(gameId, selectedFile);
            const updatedGameData = await getVideoGameById(gameId);
            setGameData(updatedGameData);
            setSubmitted(true);
            setSelectedFile(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    if (loading && !gameData) {
        return <div className="section-status">Loading Portal...</div>;
    }

    return (
        <main className="main" style={{ paddingTop: '40px' }}>
            <section className="section">
                <div className="section-header">
                    <div>
                        <h1 className="section-title">Upload Portal</h1>
                        <p className="hero-sub" style={{ marginBottom: 0 }}>Adding assets for '{gameData?.name || "Game"}'</p>
                    </div>
                    {submitted && (
                        <div style={{ color: '#00ff7f', fontWeight: 700 }}>✓ File uploaded successfully!</div>
                    )}
                </div>

                <div className="game-card" style={{ padding: '40px', marginTop: '30px' }}>
                    <div style={{ display: 'flex', gap: '20px', marginBottom: '30px' }}>
                        {devId && (
                            <div style={{ padding: '12px 16px', background: 'rgba(255,255,255,0.03)', borderRadius: '6px', fontSize: '0.85rem' }}>
                                <span style={{ color: 'var(--text-secondary)' }}>Developer ID:</span> <span style={{ fontWeight: 700 }}>{devId}</span>
                            </div>
                        )}
                        {gameId && (
                            <div style={{ padding: '12px 16px', background: 'rgba(255,255,255,0.03)', borderRadius: '6px', fontSize: '0.85rem' }}>
                                <span style={{ color: 'var(--text-secondary)' }}>Game ID:</span> <span style={{ fontWeight: 700 }}>{gameId}</span>
                            </div>
                        )}
                    </div>

                    {error && (
                        <div className="section-status error" style={{ marginBottom: '24px' }}>
                            {error}
                        </div>
                    )}

                    <div style={{ marginBottom: '40px' }}>
                        <h3 className="game-card-title" style={{ marginBottom: '16px' }}>Existing Files</h3>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
                            {gameData?.files && gameData.files.length > 0 ? (
                                gameData.files.map((file) => (
                                    <div key={file} style={{ padding: '6px 12px', background: 'var(--blue-deeper)', border: '1px solid var(--blue-border)', borderRadius: '4px', fontSize: '0.85rem' }}>
                                        📁 {file}
                                    </div>
                                ))
                            ) : (
                                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>No files uploaded yet.</p>
                            )}
                        </div>
                    </div>

                    <form onSubmit={onFileUpload} className="auth-form">
                        <div className="form-group">
                            <label className="form-label">Select File to Upload</label>
                            <input
                                className="form-input"
                                type="file"
                                onChange={onFileChange}
                                required
                                style={{ padding: '10px' }}
                            />
                            {selectedFile && (
                                <p style={{ marginTop: '10px', fontSize: '0.85rem', color: 'var(--blue)' }}>
                                    Ready to upload: <strong>{selectedFile.name}</strong> ({(selectedFile.size / 1024 / 1024).toFixed(2)} MB)
                                </p>
                            )}
                        </div>

                        <button type="submit" className="btn btn-red btn-full" disabled={loading || !selectedFile} style={{ marginTop: '20px' }}>
                            {loading ? "Uploading..." : "Upload File"}
                        </button>
                    </form>
                </div>
            </section>
        </main>
    )
}
