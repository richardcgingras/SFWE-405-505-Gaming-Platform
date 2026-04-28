import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

/**
 * Shared navbar used on every page.
 * - Logo always links home.
 * - Middle links: Store | Library | Community | News (never change).
 * - Right: avatar + username + Log Out when logged in, Log In + Sign Up when not.
 */
export default function Navbar() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const username = localStorage.getItem("username") || "";
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    setIsLoggedIn(false);
    navigate("/login");
  };

  return (
    <nav className="nav">
      {/* ── Logo → home ── */}
      <div className="nav-logo">
        <Link to="/">
          <span className="logo-icon"></span>
          <span className="logo-text">good<span>Gamers</span></span>
        </Link>
      </div>

      {/* ── Fixed 4 links ── */}
      <ul className="nav-links">
        <li><Link to="/store">Store</Link></li>
        <li><Link to="/library">Library</Link></li>
        <li><Link to="/community">Community</Link></li>
        <li><a href="/news">News</a></li>
      </ul>

      {/* ── Auth area ── */}
      <div className="nav-actions">
        {isLoggedIn ? (
          <>
            <div className="nav-avatar">{username?.[0]?.toUpperCase()}</div>
            <span className="nav-username">{username}</span>
            <button className="btn btn-ghost" onClick={handleLogout}>Log Out</button>
          </>
        ) : (
          <>
            <Link to="/login" className="btn btn-ghost">Log In</Link>
            <Link to="/signup" className="btn btn-red">Sign Up</Link>
          </>
        )}
      </div>
    </nav>
  );
}
