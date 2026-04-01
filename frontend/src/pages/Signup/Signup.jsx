import "./Signup.css";

export default function Signup() {
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
          <h2 className="auth-title">Create Account</h2>
          <p className="auth-sub">Join millions of players on goodGamers</p>

          <div className="auth-form">
            <div className="form-group">
              <label className="form-label">Username</label>
              <input className="form-input" type="text" placeholder="YourGamerTag" />
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" type="email" placeholder="you@email.com" />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <input className="form-input" type="password" placeholder="••••••••" />
            </div>
            <div className="form-group">
              <label className="form-label">Confirm Password</label>
              <input className="form-input" type="password" placeholder="••••••••" />
            </div>
            <button className="btn btn-red btn-full">Create Account</button>
            <p className="auth-switch">
              Already have an account? <a href="/login">Log In</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}