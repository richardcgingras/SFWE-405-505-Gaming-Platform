import { useState, useEffect } from "react";
import { getVideoGameById, getFile } from "../../services/VideoGame.js";

export default function Download() {
  const [game, setGame] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [downloading, setDownloading] = useState(null);

  useEffect(() => {
    const fetchGame = async () => {
      try {
        setLoading(true);
        const getQueryParams = () => new URLSearchParams(window.location.search);
        const gameId = parseInt(getQueryParams().get('id')) || null;
        if (!gameId) throw new Error("No game ID provided.");
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
    setDownloading(file);
    try {
      const response = await getFile(game.id, file);
      if (!(response instanceof Blob)) throw new Error("Could not retrieve file data.");

      const filename = `${game.name}-${file}`;

      if ('showSaveFilePicker' in window) {
        try {
          const handle = await window.showSaveFilePicker({
            suggestedName: filename,
            types: [{ description: 'Game Files', accept: { '*/*': ['.zip', '.exe', '.png'] } }],
          });
          const writable = await handle.createWritable();
          await writable.write(response);
          await writable.close();
          return;
        } catch (err) {
          if (err.name === 'AbortError') return;
        }
      }

      const url = URL.createObjectURL(response);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (err) {
      setError("Download error: " + err.message);
    } finally {
      setDownloading(null);
    }
  };

  if (loading) return <div className="section-status">Loading Game Files...</div>;
  if (error) return <div className="section-status error">Error: {error}</div>;

  return (
    <main className="main" style={{ paddingTop: '40px' }}>
      <div className="section-header">
        <h2 className="section-title">Download Files - {game?.name}</h2>
      </div>
      {!game?.files || game.files.length === 0 ? (
        <p className="hero-sub">This game contains no files.</p>
      ) : (
        <div className="games-grid">
          {game.files.map((file) => (
            <div key={file} className="game-card">
              <div className="game-card-info">
                <h3 className="game-card-title">📁 {file}</h3>
                <button 
                  className="btn btn-red" 
                  onClick={() => handleFileDownload(file)}
                  disabled={downloading === file}
                >
                  {downloading === file ? "Preparing..." : "Download"}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </main>
  );
}
