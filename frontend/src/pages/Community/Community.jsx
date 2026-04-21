import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Community.css";

function base64UrlDecodeJson(input) {
  const normalized = input.replace(/-/g, "+").replace(/_/g, "/");
  const padLength = (4 - (normalized.length % 4)) % 4;
  const padded = normalized + "=".repeat(padLength);
  return JSON.parse(atob(padded));
}

function getUserIdFromToken(token) {
  try {
    const parts = token.split(".");
    if (parts.length < 2) return null;
    const payload = base64UrlDecodeJson(parts[1]);
    const sub = payload?.sub;
    if (sub == null) return null;
    const id = Number(sub);
    return Number.isFinite(id) ? id : null;
  } catch {
    return null;
  }
}

function normalizeStatus(raw) {
  const s = String(raw || "").trim().toLowerCase();
  if (!s) return { label: "Unknown", tone: "unknown" };
  if (["online", "active", "available"].includes(s)) return { label: "Online", tone: "online" };
  if (["offline", "inactive", "away", "busy"].includes(s)) return { label: s[0].toUpperCase() + s.slice(1), tone: s };
  return { label: s[0].toUpperCase() + s.slice(1), tone: "unknown" };
}

export default function Community() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [profile, setProfile] = useState(null);

  const token = useMemo(() => localStorage.getItem("token"), []);
  const userId = useMemo(() => (token ? getUserIdFromToken(token) : null), [token]);

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }
    if (!userId) {
      localStorage.removeItem("token");
      navigate("/login");
      return;
    }

    let cancelled = false;

    const fetchProfile = async () => {
      try {
        setError("");
        const res = await fetch(`/api/user-profiles/${userId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.status === 401) {
          localStorage.removeItem("token");
          navigate("/login");
          return;
        }
        if (!res.ok) {
          throw new Error(`Failed to load profile (${res.status})`);
        }

        const data = await res.json();
        if (!cancelled) {
          setProfile(data);
          setLoading(false);
        }
      } catch (e) {
        if (!cancelled) {
          setError(e?.message || "Failed to load friends list.");
          setLoading(false);
        }
      }
    };

    fetchProfile();
    const interval = setInterval(fetchProfile, 10_000);

    return () => {
      cancelled = true;
      clearInterval(interval);
    };
  }, [navigate, token, userId]);

  const friends = Array.isArray(profile?.friends) ? profile.friends : [];

  return (
    <div className="page">
      <div className="bg-glow" />

      <nav className="nav">
        <div className="nav-logo">
          <span className="logo-icon">🎮</span>
          <span className="logo-text">
            good<span>Gamers</span>
          </span>
        </div>

        <ul className="nav-links">
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/games">Games</Link>
          </li>
          <li>
            <Link to="/community">Community</Link>
          </li>
        </ul>

        <div className="nav-actions">
          <button
            className="btn btn-ghost"
            onClick={() => {
              localStorage.removeItem("token");
              navigate("/login");
            }}
          >
            Logout
          </button>
        </div>
      </nav>

      <main className="main">
        <section className="section">
          <div className="community-header">
            <div>
              <h2 className="section-title">Friends</h2>
              <div className="community-sub">
                {profile?.userName ? `Signed in as ${profile.userName}` : "Your friends list and presence"}
              </div>
            </div>
            <div className="community-count">{friends.length} Friends</div>
          </div>

          {loading && <div className="community-state">LOADING...</div>}
          {!loading && error && (
            <div className="community-state error">
              {error}
              <div className="community-hint">
                If this fails with a JSON recursion error, we’ll need to adjust backend serialization for the self-referencing
                `friends` relationship.
              </div>
            </div>
          )}
          {!loading && !error && friends.length === 0 && (
            <div className="community-state">No friends yet. Add some friends to see their status here.</div>
          )}

          {!loading && !error && friends.length > 0 && (
            <div className="friends-grid">
              {friends.map((f) => {
                const { label, tone } = normalizeStatus(f?.status);
                return (
                  <div key={f?.id ?? `${f?.userName}-${label}`} className="friend-card">
                    <div className="friend-main">
                      <div className="friend-avatar">
                        <span>{String(f?.userName || "?").slice(0, 1).toUpperCase()}</span>
                      </div>
                      <div className="friend-meta">
                        <div className="friend-name">{f?.userName || "Unknown user"}</div>
                        <div className="friend-email">{f?.email || ""}</div>
                      </div>
                    </div>
                    <div className={`status-pill ${tone}`}>{label}</div>
                  </div>
                );
              })}
            </div>
          )}
        </section>
      </main>

      <footer className="footer">
        <div className="footer-logo">goodGamers</div>
        <div className="footer-copy">© 2026 Gaming Platform</div>
      </footer>
    </div>
  );
}
