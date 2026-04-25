import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getLibraryById } from "../../services/GameLibrary.js";
import "./Library.css";

// Add authentication context or use localStorage for auth state
const isAuthenticated = !!localStorage.getItem("token"); // Example check
const getId = () => localStorage.getItem("userId") || "";

export default function Library() {
    const [library, setLibrary] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // Initialize navigator

    useEffect(() => {
        const fetchLibrary = async () => {
        try {
            setLoading(true);
            const data = await getLibraryById(getId());
            setLibrary(data || []); // Handle empty response safely
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
        return <div className="library-loading">Loading your library...</div>;
    }

    if (error) {
        return <div className="library-error">Error: {error}</div>;
    }

    console.log(library);
    console.log("Library Qty: " + library.owner.gameLibrary.length);

    return (
        <div className="library-page">
        <h1>Your Game Library</h1>
        {library.owner.gameLibrary.length === 0 ? (
            <p className="library-empty-text">Your library is empty.</p>
        ) : (
            <ul className="library-grid">
                {library.owner.gameLibrary.map((game) => (
                <li
                    key={game.id}
                    className="library-game-card"
                    onClick={() => navigate(`/Download?id=${game.id}`)} // Added click handler
                >
                <h3>{game.name}</h3>
                <span className="system">
                    {game.system || "Error"}<br></br>
                </span>
                <span className="size">
                    {game.size || "Error"}GB<br></br>
                </span>
                <span className="rating">
                    {game.ageRating || "Error"}
                    <br></br>
                </span>
                </li>
            ))}
        </ul>
        )}
    </div>
    );
}
