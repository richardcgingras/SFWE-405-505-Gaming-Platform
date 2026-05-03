import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Library.css";

export default function Library() {
  const [games, setGames] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [downloading, setDownloading] = useState(null);
  const [toast, setToast] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    if (!token) {
      navigate("/login");
      return;
    }

    if (!userId || userId === "null") {
      setError("Please log in again to access your library.");
      setLoading(false);
      return;
    }

    const fetchLibrary = async () => {
      try {
        setLoading(true);
        const response = await fetch(`/api/user-profiles/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!response.ok) throw new Error(`Failed to load library (HTTP ${response.status})`);

        const data = await response.json();
        setGames(Array.isArray(data.gameLibrary) ? data.gameLibrary : []);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchLibrary();
  }, [navigate]);

  const showToast = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(""), 3500);
  };

  const handleDownload = async (game) => {
    setDownloading(game.id);
    try {
      const coin = Math.random() < 0.5 ? "png1" : "png2";
      const url = `/game-assets/${coin}.png`;

      const response = await fetch(url);
      if (!response.ok) {
        showToast("⚠️ Download asset missing in public/game-assets/");
        return;
      }

      const blob = await response.blob();
      const filename = `${game.name.replace(/\s+/g, "_")}_installer.png`;

      // NATIVE SAVE DIALOG (showSaveFilePicker)
      if ('showSaveFilePicker' in window) {
        try {
          const handle = await window.showSaveFilePicker({
            suggestedName: filename,
            types: [{
              description: 'PNG Image',
              accept: {'image/png': ['.png']},
            }],
          });
          const writable = await handle.createWritable();
          await writable.write(blob);
          await writable.close();
          showToast(`⬇️ ${game.name} saved successfully!`);
          return;
        } catch (err) {
          if (err.name === 'AbortError') return; 
          console.warn("Save picker failed, falling back", err);
        }
      }

      // FALLBACK DOWNLOAD
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(link.href);
      showToast(`⬇️ Downloading ${game.name}…`);
    } catch (err) {
      showToast("Download failed. Check the console.");
      console.error(err);
    } finally {
      setDownloading(null);
    }
  };

  return (
    <main className="main" style={{ paddingTop: "40px" }}>
      {toast && <div className="lib-toast">{toast}</div>}

      <div className="section-header">
        <h2 className="section-title">Your Game Library</h2>
      </div>

      {loading && <div className="library-loading">Loading your library...</div>}
      {error && <div className="library-error" style={{ color: 'var(--red)' }}>Error: {error}</div>}

      {!loading && !error && games.length === 0 && (
        <div className="library-empty-state">
          <div className="library-empty-icon">🎮</div>
          <h3 className="library-empty-title">Your library is empty</h3>
          <p className="library-empty-text">Check out the store to buy some games!</p>
          <a href="/store" className="btn btn-red" style={{ marginTop: '16px', display: 'inline-block' }}>
            Browse the Store
          </a>
        </div>
      )}

      {!loading && !error && games.length > 0 && (
        <div className="library-grid">
          {games.map((game) => (
            <div key={game.id} className="library-game-card">
              <h3>{game.name}</h3>
              <div className="item-details">
                {game.ageRating && <span className="age-rating">{game.ageRating}</span>}
                {game.size != null && <span className="file-size">{game.size} GB</span>}
              </div>
              <button
                className="btn btn-red lib-download-btn"
                onClick={() => handleDownload(game)}
                disabled={downloading === game.id}
              >
                {downloading === game.id ? "Preparing..." : "⬇ Download"}
              </button>
            </div>
          ))}
        </div>
      )}
    </main>
  );
}
