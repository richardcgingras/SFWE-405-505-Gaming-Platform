import "./Library.css";

import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getLibraryById } from "../../services/GameLibrary.js";

export default function Library() {
    const [library, setLibrary] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // Initialize navigator

    useEffect(() => {
        const fetchLibrary = async () => {
            try {
                setLoading(true);
                const data = await getLibraryById(101);
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
    console.log("Library Qty: " + library.games.length);

    return (
        <div className="library-page">
        <h1>Your Game Library</h1>
        {library.games.length === 0 ? (
            <p className="library-empty-text">Your library is empty.</p>
        ) : (
            <ul className="library-grid">
                {library.games.map((game) => (
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
