import "./Publish.css";

import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { createVideoGame } from "../../services/VideoGame.js";
import { addGameToDeveloper } from "../../services/Developer.js";

export default function Publish() {
    const [devId, setDevId] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitted, setSubmitted] = useState(false);
    const navigate = useNavigate();

    // Form data for new video game
    const [formData, setFormData] = useState({
        name: "",
        releaseDate: new Date().toISOString().split("T")[0],
        price: "",
        systems: "",
        size: "",
        publisher: null
    });

    useEffect(() => {
        const fetchDev = async () => {
            try {
                setLoading(true);
                const getQueryParams = () => new URLSearchParams(window.location.search);
                const devId = parseInt(getQueryParams().get("devId")) || null;
                console.log(devId)
                setDevId(devId || []);
                setError(null);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchDev();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSubmitted(false);

        try {
            const systems = formData.systems.split(",").map((s) => s.trim()).filter(Boolean);

            const gameData = {
                name: formData.name,
                releaseDate: new Date(formData.releaseDate),
                files: null,
                system: systems.length > 0 ? systems.map((s) => parseInt(s)) : [],
                price: parseFloat(formData.price || 0),
                size: parseFloat(formData.size || 0),
                ageRating: null
            };

            console.log("Submitting video game data:", gameData);

            const createResponse = await createVideoGame(gameData);

            if (!createResponse)
                throw new Error("Failed to create video game");

            const addResponse = await addGameToDeveloper(devId, createResponse.id); 

            if (addResponse) {
                setSubmitted(true);
                setError(null);
                setTimeout(() => {
                    // Navigate to upload page instead of publish portal
                    navigate("/upload?devId="+devId+"&gameId="+createResponse.id); 
                }, 2000);
            } else {
                throw new Error("No response received from backend");
            }
        } catch (err) {
            console.error("Create video game failed:", err);
            setError(err.message || "Failed to create video game");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const handleNavigation = () => setSubmitted(false);
        window.addEventListener("popstate", handleNavigation);
        return () => window.removeEventListener("popstate", handleNavigation);
    }, []);

    if (loading) {
        return <div className="loading-spinner">Loading...</div>;
    }

    return (
        <div className="page" style={{ minHeight: '100vh', color: '#e8edf5', fontFamily: 'Barlow, sans-serif', backgroundImage: 'radial-gradient(circle at top left, rgba(21, 101, 192, 0.4), transparent)' }}>
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

            <section className="hero" style={{ position: 'relative', zIndex: 1, padding: '40px 60px 20px', width: '100%' }}>
                <div className="hero-content">
                    <h1 className="hero-title" style={{ marginTop: 0 }}>Publish Portal</h1>
                    {submitted && (
                        <p style={{ color: '#7a96b8' }}>✓ Video game published successfully!</p>
                    )}
                </div>
            </section>

            <main className="main" style={{ padding: '20px 60px', background: 'var(--blue-deeper)' }}>
                <div className="publish-container">
                    
                    {devId.length > 0 && (
                        <div className="info-box">
                            <p><strong>Developer ID:</strong> {devId}</p>
                            <p>Please create a new video game to publish.</p>
                        </div>
                    )}

                    {error && (
                        <div className="error-message" style={{ background: '#ffebee', color: '#c62828' }}>
                            Error: {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="publish-form">
                        <div className="form-group">
                            <label htmlFor="name">Game Name:</label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                                required
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label htmlFor="releaseDate">Release Date:</label>
                                <input
                                    type="date"
                                    id="releaseDate"
                                    name="releaseDate"
                                    value={formData.releaseDate}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="price">Price ($):</label>
                                <input
                                    type="number"
                                    id="price"
                                    name="price"
                                    min="0"
                                    step="0.01"
                                    value={formData.price}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label htmlFor="size">Size (GB):</label>
                                <input
                                    type="number"
                                    id="size"
                                    name="size"
                                    min="0.1"
                                    step="0.1"
                                    value={formData.size}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="ageRating">Age Rating:</label>
                                <select
                                    id="ageRating"
                                    name="ageRating"
                                    value={formData.ageRating}
                                    onChange={handleInputChange}
                                    required
                                >
                                    <option value="">Select rating...</option>
                                    <option value="E">Everyone (E)</option>
                                    <option value="E10+">Everyone 10+</option>
                                    <option value="T">Teen (T)</option>
                                    <option value="M">Mature (M)</option>
                                    <option value="AO">Adults Only (AO)</option>
                                </select>
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="systems">Console Systems:</label>
                            <input
                                type="text"
                                id="systems"
                                name="systems"
                                placeholder="e.g., PC, Steam Deck or leave empty for all platforms"
                                value={formData.systems}
                                onChange={handleInputChange}
                            />
                        </div>

                        <button type="submit" className={`submit-btn ${loading ? 'disabled' : ''}`}>
                            {loading ? "Publishing..." : "Publish Video Game"}
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
    );
}
