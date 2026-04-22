import { Link } from 'react-router-dom'
import "./App.css";

//this is the sample games we will need to eventually make it pull from the backend and display something there
const games = [
  { id: 1, title: "Phantom Siege", genre: "FPS / Tactical", price: 59.99 },
  { id: 2, title: "Neon Abyss II", genre: "RPG / Action", price: 44.99 },
  { id: 3, title: "StarForge", genre: "Strategy / Sci-Fi", price: 39.99 },
  { id: 4, title: "Iron Realm", genre: "MMORPG", price: 0 },
];

export default function Home() {
  return (
    //Header and nav bar
    <div className="page">
      <div className="bg-glow" />

      <nav className="nav">
        <div className="nav-logo">
          <span className="logo-icon"></span>
          <span className="logo-text">good<span>Gamers</span></span>
        </div>
        <ul className="nav-links">
          <li><Link to="/">Store</Link></li>
          <li><a href="#">Library</a></li>
          <li><Link to="/community">Community</Link></li>
          <li><a href="#">News</a></li>
        </ul>
        <div className="nav-actions">
          <Link to="/login" className="btn btn-ghost">Log In</Link>
          <Link to="/signup" className="btn btn-red">Sign Up</Link>
        </div>
      </nav>
    {/*The left sections of the home page */}
      <section className="hero">
        <div className="hero-content">
          <div className="hero-tag">14.2M Players Online Now</div>
          <h1 className="hero-title">
            YOUR UNIVERSE.<br />
            <span className="hero-title-accent">YOUR RULES.</span>
          </h1>
          <p className="hero-sub">
            The ultimate gaming platform — play, buy, and compete with friends across thousands of worlds.
          </p>
          <div className="hero-cta">
            <button className="btn btn-red">Browse Store →</button>
            <button className="btn btn-ghost">Watch Trailer ▶</button>
          </div>
        </div>
      </section>

    {/*status bar with the stats of the games and players and stuff this will also need to be pulled from the backend */}
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

             {/*The list of games that will be displayed which will eventually come from the backend  */}
            <h2 className="section-title">Featured Games</h2>
            <Link to="/games" className="section-link"> View All → </Link>
          </div>
          <div className="games-grid">
            {games.map((game, i) => (
              <div key={game.id} className="game-card" style={{ animationDelay: `${i * 0.1}s` }}>
                <div className="game-card-info">
                  <h3 className="game-card-title">{game.title}</h3>
                  <span className="game-price">
                    {game.price === 0 ? "FREE" : `$${game.price}`}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </section>
      </main>


            
      {/*All the boring stuff at bottom of the page*/}
      <footer className="footer">
        <div className="footer-logo"> goodGamers</div>
        <p className="footer-copy"> 2025 goodGamers Inc. SFWE 405. The university of Arizona</p>
        <div className="footer-links">
          <a href="#">Privacy</a>
          <a href="#">Terms</a>
          <a href="#">Support</a>
        </div>
      </footer>
    </div>
  );
}