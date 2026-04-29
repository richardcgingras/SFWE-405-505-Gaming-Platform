import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const getToken = () => localStorage.getItem("token") || "";

const getCurrentUser = () => {
  const token = getToken();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return {
      id: payload.sub,
      username: localStorage.getItem("username") || "User",
    };
  } catch {
    return null;
  }
};

const Navbar = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    setUser(getCurrentUser());
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    setUser(null);
    navigate("/");
  };

  return (
    <nav className="nav">
      <div className="nav-logo">
        <Link to={user ? "/home" : "/"}>
          <span className="logo-icon"></span>
          <span className="logo-text">good<span>Gamers</span></span>
        </Link>
      </div>
      <ul className="nav-links">
        <li><Link to="/store">Store</Link></li>
        {user && <li><Link to="/library">Library</Link></li>}
        {user && <li><Link to="/wishlist">Wishlist</Link></li>}
        {user && <li><Link to="/cart">Cart</Link></li>}
        <li><Link to="/community">Community</Link></li>
        <li><Link to="/news">News</Link></li>
      </ul>
      <div className="nav-actions">
        {user ? (
            <div className="nav-user">
              <div className="nav-avatar">
                {user.username?.[0]?.toUpperCase()}
              </div>
              <span className="nav-username">{user.username}</span>
            <button className="btn btn-ghost" onClick={handleLogout}>
                Log Out
              </button>
            </div>
        ) : (
          <>
            <Link to="/login" className="btn btn-ghost">Log In</Link>
            <Link to="/signup" className="btn btn-red">Sign Up</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
