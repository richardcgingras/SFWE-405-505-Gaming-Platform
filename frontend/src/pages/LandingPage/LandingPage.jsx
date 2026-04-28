import { Link, useNavigate } from "react-router-dom";
import { useEffect } from "react";

const getToken = () => localStorage.getItem("token") || "";

export default function LandingPage() {
  const navigate = useNavigate();

  useEffect(() => {
    if (getToken()) {
      navigate("/home");
    }
  }, [navigate]);

  return (
    <>
      <section className="hero">
        <div className="hero-content">
          <div className="hero-tag">14.2M Players Online Now</div>
          <h1 className="hero-title">
            YOUR UNIVERSE.<br />
            <span className="hero-title-accent">YOUR RULES.</span>
          </h1>
          <p className="hero-sub">
            The ultimate gaming platform — play, buy, and compete with friends
            across thousands of worlds.
          </p>
          <div className="hero-cta">
            <Link to="/signup" className="btn btn-red">Join Now — It's Free</Link>
            <button className="btn btn-ghost">Watch Trailer ▶</button>
          </div>
        </div>
      </section>

      <div className="stats-bar">
        {[
          { label: "Games", val: "12,400+" },
          { label: "Players", val: "14.2M" },
          { label: "Daily Deals", val: "340" },
          { label: "Countries", val: "180+" },
        ].map((s) => (
          <div key={s.label} className="stat">
            <span className="stat-val">{s.val}</span>
            <span className="stat-label">{s.label}</span>
          </div>
        ))}
      </div>

      <main className="main">
        <section className="section">
          <div className="section-header">
            <h2 className="section-title">Why goodGamers?</h2>
          </div>
          <div className="games-grid">
            <div className="game-card" style={{ padding: '30px' }}>
              <h3 className="game-card-title">Massive Library</h3>
              <p style={{ color: 'var(--text-secondary)', marginTop: '10px' }}>Access over 12,000 titles from indie gems to AAA blockbusters.</p>
            </div>
            <div className="game-card" style={{ padding: '30px' }}>
              <h3 className="game-card-title">Vibrant Community</h3>
              <p style={{ color: 'var(--text-secondary)', marginTop: '10px' }}>Join millions of players in discussions, groups, and events.</p>
            </div>
            <div className="game-card" style={{ padding: '30px' }}>
              <h3 className="game-card-title">Instant Downloads</h3>
              <p style={{ color: 'var(--text-secondary)', marginTop: '10px' }}>Experience lightning-fast downloads and automatic updates.</p>
            </div>
            <div className="game-card" style={{ padding: '30px' }}>
              <h3 className="game-card-title">Cross-Platform</h3>
              <p style={{ color: 'var(--text-secondary)', marginTop: '10px' }}>Play with your friends regardless of what device they use.</p>
            </div>
          </div>
        </section>
      </main>
    </>
  );
}
