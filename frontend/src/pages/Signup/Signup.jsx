import { useState } from "react";
import "./Signup.css";

// ── mirrors backend PasswordValidator regex ──────────────────────────────────
const rules = [
  {
    id: "length",
    label: "8–12 characters",
    test: (p) => p.length >= 8 && p.length <= 12,
  },
  {
    id: "upper",
    label: "At least 1 uppercase letter",
    test: (p) => /[A-Z]/.test(p),
  },
  {
    id: "number",
    label: "At least 1 number",
    test: (p) => /\d/.test(p),
  },
  {
    id: "special",
    label: "At least 1 special character (!@#$%^&* …)",
    test: (p) => /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(p),
  },
];

const isPasswordValid = (p) => rules.every((r) => r.test(p));

export default function Signup() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);
  const [passwordFocused, setPasswordFocused] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage("");
    setIsError(false);

    // ── client-side guards ───────────────────────────────────────────────────
    if (!isPasswordValid(password)) {
      setMessage("Password does not meet the required criteria.");
      setIsError(true);
      return;
    }

    if (password !== confirmPassword) {
      setMessage("Passwords do not match.");
      setIsError(true);
      return;
    }

    // ── submit ───────────────────────────────────────────────────────────────
    const response = await fetch("/api/user-profiles", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userName: username,
        email,
        password,
        status: "active",
      }),
    });

    if (response.status === 201) {
      setMessage("Account created successfully. You can now log in.");
      setIsError(false);
      setUsername("");
      setEmail("");
      setPassword("");
      return;
    }

    if (response.status === 409) {
      setMessage("This email is already in the database. Please use a different email.");
      setIsError(true);
      return;
    }

    const body = await response.text();
    setMessage(body || "Unable to create account. Please try again.");
    setIsError(true);
  };

  return (
    <div className="page">
      <div className="bg-glow" />

      <nav className="nav">
        <div className="nav-logo">
          <a href="/">
            <span className="logo-icon"></span>
            <span className="logo-text">good<span>Gamers</span></span>
          </a>
        </div>
        <div className="nav-actions">
          <a href="/login" className="btn btn-ghost">Log In</a>
        </div>
      </nav>

      <div className="auth-container">
        <div className="auth-box">
          <h2 className="auth-title">Create Account</h2>
          <p className="auth-sub">Join millions of players on goodGamers</p>

          <form className="auth-form" onSubmit={handleSubmit}>
            {message && (
              <div className={`auth-message ${isError ? "error" : "success"}`}>
                {message}
              </div>
            )}

            <div className="form-group">
              <label className="form-label">Username</label>
              <input
                className="form-input"
                type="text"
                placeholder="YourGamerTag"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Email</label>
              <input
                className="form-input"
                type="email"
                placeholder="you@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Password</label>
              <input
                className="form-input"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <button className="btn btn-red btn-full" type="submit">
              Create Account
            </button>

            <p className="auth-switch">
              Already have an account? <a href="/login">Log In</a>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}