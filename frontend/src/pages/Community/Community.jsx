import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../../components/Navbar/Navbar.jsx";
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
  const [addEmail, setAddEmail] = useState("");
  const [addBusy, setAddBusy] = useState(false);
  const [addMessage, setAddMessage] = useState("");

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

  const refreshProfile = async () => {
    if (!token || !userId) return;
    const res = await fetch(`/api/user-profiles/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (res.status === 401) {
      localStorage.removeItem("token");
      navigate("/login");
      return;
    }
    if (!res.ok) {
      throw new Error(`Failed to refresh profile (${res.status})`);
    }
    const data = await res.json();
    setProfile(data);
  };

  const handleAddFriend = async (e) => {
    e.preventDefault();
    if (!token || !userId) return;

    const email = addEmail.trim();
    setAddMessage("");

    if (!email) {
      setAddMessage("Enter an email address.");
      return;
    }

    setAddBusy(true);
    try {
      // 1) Lookup friend by email (backend already supports this)
      const lookupRes = await fetch(`/api/user-profiles/email/${encodeURIComponent(email)}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (lookupRes.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }
      if (lookupRes.status === 404) {
        setAddMessage("No user found with that email.");
        return;
      }
      if (!lookupRes.ok) {
        throw new Error(`Lookup failed (${lookupRes.status})`);
      }

      const friend = await lookupRes.json();
      const friendId = friend?.id;
      if (!friendId) {
        throw new Error("Lookup response missing friend id.");
      }
      if (Number(friendId) === Number(userId)) {
        setAddMessage("You can’t add yourself.");
        return;
      }
      if (friends.some((f) => Number(f?.id) === Number(friendId))) {
        setAddMessage("That user is already on your friends list.");
        return;
      }

      // 2) Add friend by id (endpoint we patched)
      const addRes = await fetch(`/api/user-profiles/${userId}/friends/${friendId}`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (addRes.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }
      if (!addRes.ok) {
        throw new Error(`Add friend failed (${addRes.status})`);
      }

      setAddEmail("");
      setAddMessage("Friend added.");
      await refreshProfile();
    } catch (err) {
      setAddMessage(err?.message || "Unable to add friend.");
    } finally {
      setAddBusy(false);
    }
  };

  return (
    <div className="page">
      <div className="bg-glow" />

      <Navbar />

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

          {!loading && !error && (
            <div className="add-friend-card">
              <div className="add-friend-title">Add Friend</div>
              <form className="add-friend-form" onSubmit={handleAddFriend}>
                <input
                  className="add-friend-input"
                  type="email"
                  placeholder="friend@email.com"
                  value={addEmail}
                  onChange={(ev) => setAddEmail(ev.target.value)}
                  disabled={addBusy}
                />
                <button className="btn btn-red" type="submit" disabled={addBusy}>
                  {addBusy ? "Adding..." : "Add"}
                </button>
              </form>
              {addMessage && <div className="add-friend-message">{addMessage}</div>}
            </div>
          )}

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
