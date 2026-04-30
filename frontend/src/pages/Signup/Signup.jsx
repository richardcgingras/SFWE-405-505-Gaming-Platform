import { useState } from "react";

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
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isError, setIsError] = useState(false);
  const [passwordFocused, setPasswordFocused] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage("");
    setIsError(false);

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

    try {
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
        setConfirmPassword("");
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
    } catch (error) {
      setMessage(error.message || "Unable to create account. Please try again.");
      setIsError(true);
    }
  };

  const passwordTouched = password.length > 0;
  const confirmMismatch = confirmPassword.length > 0 && password !== confirmPassword;

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h2 className="auth-title">Create Account</h2>
        <p className="auth-sub">Join millions of players on goodGamers</p>

        <div className="auth-form">
          {message && (
            <div className={`auth-message ${isError ? "error" : "success"}`}>
              {message}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Username</label>
              <input
                className="form-input"
                type="text"
                id="signup-username"
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
                id="signup-email"
                placeholder="you@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Password</label>
              <input
                className={`form-input ${
                  passwordTouched
                    ? isPasswordValid(password)
                      ? "input-valid"
                      : "input-invalid"
                    : ""
                }`}
                type="password"
                id="signup-password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                onFocus={() => setPasswordFocused(true)}
                onBlur={() => setPasswordFocused(false)}
                required
              />

              {(passwordFocused || passwordTouched) && (
                <ul className="pw-checklist">
                  {rules.map((rule) => {
                    const passed = rule.test(password);
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

            <div className="form-group">
              <label className="form-label">Confirm Password</label>
              <input
                className={`form-input ${
                  confirmPassword.length > 0
                    ? confirmMismatch
                      ? "input-invalid"
                      : "input-valid"
                    : ""
                }`}
                type="password"
                id="signup-confirm-password"
                placeholder="••••••••"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
              {confirmMismatch && (
                <span className="pw-mismatch">Passwords do not match</span>
              )}
            </div>

            <button className="btn btn-red btn-full" type="submit">
              Create Account
            </button>
          </form>

          <p className="auth-switch">
            Already have an account? <a href="/login">Log In</a>
          </p>
        </div>
      </div>
    </div>
  );
}