import "./Upload.css";

import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { uploadFile, getVideoGameById } from "../../services/VideoGame.js";


export default function Upload(){
    const [devId, setDevId] = useState([]);
    const [gameId, setGameId] = useState([]);
    const [gameData, setGameData] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitted, setSubmitted] = useState(false);
    const navigate = useNavigate();
    const [selectedFile, setSelectedFile] = useState(null);

    useEffect(() => {
        const fetchDev = async () => {
            try {
                setLoading(true);
                const getQueryParams = () => new URLSearchParams(window.location.search);
                const devId = parseInt(getQueryParams().get("devId")) || null;
                const gameId = parseInt(getQueryParams().get("gameId")) || null;
                const data = await getVideoGameById(gameId);
                console.log(data);
                console.log(devId)
                console.log(gameId)
                setDevId(devId || []);
                setGameId(gameId || []);
                setGameData(data || []);
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
        if (!selectedFile) return;

        setLoading(true);
        setSubmitted(false); // Reset submission status before upload
        
        try {
            const response = await uploadFile(gameId, selectedFile);
            console.log(response);

            // Refresh game data to get updated file list
            const updatedGameData = await getVideoGameById(gameId);
            setGameData(updatedGameData);

            setSubmitted(true);
        } catch (err) {
            setError(err.message);
            setLoading(false);
            setSubmitted(false);
        } finally {
            setLoading(false);
        }
    };

    const fileData = () => {
		if (selectedFile) {
			return (
				<div className="info-box">
					<p><strong>File Name:</strong> {selectedFile.name}</p>
					<p><strong>File Type:</strong> {selectedFile.type || 'Unknown'}</p>
					<p><strong>Size:</strong> {(selectedFile.size / 1024 / 1024).toFixed(2)} MB</p>
				</div>
			);
		}
	};

    if (loading) {
        return <div className="loading-spinner">Loading...</div>;
    }

    return (
        <div className="page" style={{ minHeight: '100vh', color: '#e8edf5', fontFamily: 'Barlow, sans-serif' }}>
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
                    </div>
                </nav>

            <section className="hero">
                <div className="hero-content">
                    <h1 className="hero-title">Upload Portal</h1>
                    {submitted && (
                        <p style={{ color: '#7a96b8' }}>✓ File uploaded successfully!</p>
                    )}
                </div>
            </section>

            <main className="main" style={{ padding: '20px 60px', background: 'var(--blue-deeper)' }}>
                <div className="publish-container">

                    {devId.length > 0 && (
                        <div className="info-box">
                            <p><strong>Developer ID:</strong> {devId}</p>
                            <p><strong>Game ID:</strong> {gameId}</p>
                            <p>Upload a video game file for the developer.</p>
                        </div>
                    )}

                    {error && (
                        <div className="error-message" style={{ background: '#ffebee', color: '#c62828' }}>
                            Error: {error}
                        </div>
                    )}

                    <form onSubmit={onFileUpload} className="upload-form">
                        <h1>Uploading Files for '{gameData.name}'</h1><br></br>

                        <h3>Existing files:</h3>
                        <div className="file-list">
                            {gameData && gameData.files ? (
                                gameData.files.length === 0 ? (
                                    <p className="library-empty-text">Game contains no files.</p>
                                ) : (
                                    <ul className="upload-form">
                                        {gameData.files.map((file) => (
                                            <li key={file}>
                                                {file}
                                            </li>
                                        ))}
                                    </ul>
                                )
                            ) : (
                                <p className="library-empty-text">Game contains no files.</p>
                            )}
                        </div>

                        <div className="form-group">
                            <label htmlFor="file-upload"><br></br>Select File to Upload:</label>
                            <input
                                type="file"
                                id="file-upload"
                                onChange={onFileChange}
                                required
                            />
                            {selectedFile && (
                                <p style={{ marginTop: '0.5rem', fontSize: '0.875rem', color: '#7a96b8' }}>
                                    Selected: {selectedFile.name} ({(selectedFile.size / 1024 / 1024).toFixed(2)} MB)
                                </p>
                            )}
                        </div>

                        <button
                            type="submit"
                            className={`submit-btn ${loading ? 'disabled' : ''}`}
                            style={{ width: 'auto', padding: '1rem 2.5rem' }}
                        >
                            {loading ? "Uploading..." : "Upload File"}
                        </button>
                    </form>

                </div>
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
    )
}
