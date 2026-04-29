import { useState } from "react";
import "./ResetPassword.css";

// ── same rules as Signup & backend PasswordValidator ─────────────────────────
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

export default function ResetPassword() {
  const [username, setUsername] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);
  const [passwordFocused, setPasswordFocused] = useState(false);
  const [loading, setLoading] = useState(false);

  const passwordTouched = newPassword.length > 0;
  const confirmMismatch = confirmPassword.length > 0 && newPassword !== confirmPassword;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setIsError(false);

    if (!username.trim()) {
      setMessage("Username is required.");
      setIsError(true);
      return;
    }

    if (!isPasswordValid(newPassword)) {
      setMessage("Password does not meet the required criteria.");
      setIsError(true);
      return;
    }

    if (newPassword !== confirmPassword) {
      setMessage("Passwords do not match.");
      setIsError(true);
      return;
    }

    setLoading(true);
    try {
      const response = await fetch("/api/user-profiles/reset-password", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, newPassword }),
      });

      if (response.ok) {
        setMessage("Password reset successfully! You can now log in with your new password.");
        setIsError(false);
        setUsername("");
        setNewPassword("");
        setConfirmPassword("");
      } else if (response.status === 404) {
        setMessage("No account found with that username.");
        setIsError(true);
      } else {
        const body = await response.text();
        setMessage(body || "Something went wrong. Please try again.");
        setIsError(true);
      }
    } catch {
      setMessage("Unable to reach the server. Please try again later.");
      setIsError(true);
    } finally {
      setLoading(false);
    }
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
      </nav>

      <div className="auth-container">
        <div className="auth-box">
          {/* ── Header ── */}
          <div className="reset-icon-wrap">
            <span className="reset-icon">🔑</span>
          </div>
          <h2 className="auth-title">Reset Password</h2>
          <p className="auth-sub">
            Enter your username and choose a new password.
          </p>

          <div className="auth-form">
            {message && (
              <div className={`auth-message ${isError ? "error" : "success"}`}>
                {message}
              </div>
            )}

            <form onSubmit={handleSubmit}>

              {/* ── Username ── */}
              <div className="form-group">
                <label className="form-label">Username</label>
                <input
                  className="form-input"
                  type="text"
                  id="reset-username"
                  placeholder="YourGamerTag"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  autoComplete="username"
                  required
                />
              </div>

              {/* ── New Password ── */}
              <div className="form-group">
                <label className="form-label">New Password</label>
                <input
                  className={`form-input ${
                    passwordTouched
                      ? isPasswordValid(newPassword)
                        ? "input-valid"
                        : "input-invalid"
                      : ""
                  }`}
                  type="password"
                  id="reset-new-password"
                  placeholder="••••••••"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  onFocus={() => setPasswordFocused(true)}
                  onBlur={() => setPasswordFocused(false)}
                  autoComplete="new-password"
                  required
                />

                {/* Live checklist */}
                {(passwordFocused || passwordTouched) && (
                  <ul className="pw-checklist">
                    {rules.map((rule) => {
                      const passed = rule.test(newPassword);
                      return (
                        <li
                          key={rule.id}
                          className={`pw-rule ${passed ? "pw-rule--pass" : "pw-rule--fail"}`}
                        >
                          <span className="pw-rule-icon">{passed ? "✓" : "✕"}</span>
                          {rule.label}
                        </li>
                      );
                    })}
                  </ul>
                )}
              </div>

              {/* ── Confirm Password ── */}
              <div className="form-group">
                <label className="form-label">Confirm New Password</label>
                <input
                  className={`form-input ${
                    confirmPassword.length > 0
                      ? confirmMismatch
                        ? "input-invalid"
                        : "input-valid"
                      : ""
                  }`}
                  type="password"
                  id="reset-confirm-password"
                  placeholder="••••••••"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  autoComplete="new-password"
                  required
                />
                {confirmMismatch && (
                  <span className="pw-mismatch">Passwords do not match</span>
                )}
              </div>

              <button
                className="btn btn-red btn-full"
                type="submit"
                disabled={loading}
                id="reset-submit-btn"
              >
                {loading ? "Resetting…" : "Reset Password"}
              </button>
            </form>

            <p className="auth-switch">
              Remembered it? <a href="/login">Back to Log In</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
