import "./Login.css";

export default function Login() {
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

          <div className="auth-form">
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" type="email" placeholder="you@email.com" />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <input className="form-input" type="password" placeholder="••••••••" />
            </div>
            <button className="btn btn-red btn-full">Log In</button>
            <p className="auth-switch">
              Don't have an account? <a href="/signup">Sign Up</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}