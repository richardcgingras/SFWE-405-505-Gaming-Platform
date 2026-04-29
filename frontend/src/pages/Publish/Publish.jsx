import "./Publish.css";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createVideoGame } from "../../services/VideoGame.js";
import { addGameToDeveloper } from "../../services/Developer.js";

export default function Publish() {
    const [devId, setDevId] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [submitted, setSubmitted] = useState(false);
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: "",
        releaseDate: new Date().toISOString().split("T")[0],
        price: "",
        systems: "",
        size: "",
        ageRating: ""
    });

    useEffect(() => {
        const fetchDev = async () => {
            try {
                setLoading(true);
                const getQueryParams = () => new URLSearchParams(window.location.search);
                const devIdParam = parseInt(getQueryParams().get("devId")) || null;
                setDevId(devIdParam);
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
                system: systems.length > 0 ? systems.map((s) => parseInt(s) || s) : [],
                price: parseFloat(formData.price || 0),
                size: parseFloat(formData.size || 0),
                ageRating: formData.ageRating
            };

            const createResponse = await createVideoGame(gameData);

            if (!createResponse)
                throw new Error("Failed to create video game");

            const addResponse = await addGameToDeveloper(devId, createResponse.id); 

            if (addResponse) {
                setSubmitted(true);
                setError(null);
                setTimeout(() => {
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

    if (loading) {
        return <div className="section-status">Loading Portal...</div>;
    }

    return (
        <main className="main" style={{ paddingTop: '40px' }}>
            <section className="section">
                <div className="section-header">
                    <div>
                        <h1 className="section-title">Publish Portal</h1>
                        <p className="hero-sub" style={{ marginBottom: 0 }}>Create a new game listing</p>
                    </div>
                    {submitted && (
                        <div style={{ color: '#00ff7f', fontWeight: 700 }}>✓ Game published successfully!</div>
                    )}
                </div>

                <div className="game-card" style={{ padding: '40px', marginTop: '30px' }}>
                    {devId && (
                        <div style={{ marginBottom: '30px', padding: '16px', background: 'rgba(255,255,255,0.03)', borderRadius: '6px', fontSize: '0.9rem' }}>
                            <span style={{ color: 'var(--text-secondary)' }}>Developer ID:</span> <span style={{ fontWeight: 700 }}>{devId}</span>
                        </div>
                    )}

                    {error && (
                        <div className="section-status error" style={{ marginBottom: '24px' }}>
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="auth-form">
                        <div className="form-group">
                            <label className="form-label">Game Name</label>
                            <input
                                className="form-input"
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                                required
                                placeholder="Epic Title 2026"
                            />
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                            <div className="form-group">
                                <label className="form-label">Release Date</label>
                                <input
                                    className="form-input"
                                    type="date"
                                    name="releaseDate"
                                    value={formData.releaseDate}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Price ($)</label>
                                <input
                                    className="form-input"
                                    type="number"
                                    name="price"
                                    min="0"
                                    step="0.01"
                                    value={formData.price}
                                    onChange={handleInputChange}
                                    required
                                    placeholder="59.99"
                                />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                            <div className="form-group">
                                <label className="form-label">Size (GB)</label>
                                <input
                                    className="form-input"
                                    type="number"
                                    name="size"
                                    min="0.1"
                                    step="0.1"
                                    value={formData.size}
                                    onChange={handleInputChange}
                                    required
                                    placeholder="45.0"
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label">Age Rating</label>
                                <select
                                    className="form-input"
                                    name="ageRating"
                                    value={formData.ageRating}
                                    onChange={handleInputChange}
                                    required
                                    style={{ background: 'var(--blue-card)', color: 'var(--text-primary)' }}
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
                            <label className="form-label">Console Systems (ID or Name)</label>
                            <input
                                className="form-input"
                                type="text"
                                name="systems"
                                placeholder="e.g. 1, 2, 3"
                                value={formData.systems}
                                onChange={handleInputChange}
                            />
                        </div>

                        <button type="submit" className="btn btn-red btn-full" disabled={loading} style={{ marginTop: '20px' }}>
                            {loading ? "Publishing..." : "Publish Video Game"}
                        </button>
                    </form>
                </div>
            </section>
        </main>
    );
}
