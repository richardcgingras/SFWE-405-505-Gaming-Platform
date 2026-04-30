import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../services/Login.js"

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault()
    try {
      const data = await login(username, password);
      localStorage.setItem("token", data.accessToken);
      localStorage.setItem("username", username);
      localStorage.setItem("userId", data.userId);
      console.log("Got token: ", data.accessToken);
      navigate("/home");
      window.location.reload(); // Quick fix to refresh navbar state
    } catch (err) {
      console.error(err);
      alert("Login failed.");
    }
  };

  return (
    <div className="page">
      <div className="bg-glow" />
      <nav className="nav">
        <div className="nav-logo">
          <span className="logo-icon"></span>
          <span className="logo-text">good<span>Gamers</span></span>
        </div>
      </nav>
      <div className="auth-container">
        <div className="auth-box">
          <h2 className="auth-title">Welcome Back</h2>
          <p className="auth-sub">Log in to your goodGamers account</p>
          <form className="auth-form" onSubmit={handleLogin}>
            <div className="form-group">
              <label className="form-label">Username</label>
              <input className="form-input" type="username" placeholder="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <input className="form-input" type="password" placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
            <button className="btn btn-red btn-full">Log In</button>
            <p className="auth-switch">
              Don't have an account? <a href="/signup">Sign Up</a>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}