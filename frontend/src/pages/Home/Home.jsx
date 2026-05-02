import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

export default function Home() {
  const [username, setUsername] = useState("Gamer");
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/");
      return;
    }
    setUsername(localStorage.getItem("username") || "Gamer");
  }, [navigate]);

  const tiles = [
    { to: "/store",     icon: "🛒", label: "Store",     desc: "Browse & buy new games" },
    { to: "/library",   icon: "📚", label: "Library",   desc: "Your owned games"        },
    { to: "/community", icon: "💬", label: "Community", desc: "Chat & connect"           },
    { to: "https://www.ign.com/",      icon: "📰", label: "News",      desc: "Latest updates"           },
  ];

  return (
    <main className="main" style={{ paddingTop: "48px" }}>
      <header style={{ marginBottom: "48px" }}>
        <h1 className="hero-title" style={{ fontSize: "2.8rem", marginBottom: "10px" }}>
          Welcome back, <span className="hero-title-accent">{username}</span>
        </h1>
        <p className="hero-sub">What are we playing today?</p>
      </header>

      <section className="section">
        <div className="home-tiles">
          {tiles.map((t) => (
            <Link key={t.to} to={t.to} className="home-tile">
              <span className="home-tile-icon">{t.icon}</span>
              <span className="home-tile-label">{t.label}</span>
              <span className="home-tile-desc">{t.desc}</span>
            </Link>
          ))}
        </div>
      </section>
    </main>
  );
}