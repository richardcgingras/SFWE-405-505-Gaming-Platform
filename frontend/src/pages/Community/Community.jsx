import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

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
  const [addUsername, setAddUsername] = useState("");
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

    const username = addUsername.trim();
    setAddMessage("");

    if (!username) {
      setAddMessage("Enter a username.");
      return;
    }

    setAddBusy(true);

    try {
      const lookupRes = await fetch(`/api/user-profiles/username/${encodeURIComponent(username)}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (lookupRes.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }

      if (lookupRes.status === 404) {
        setAddMessage("No user found with that username.");
        return;
      }

      if (!lookupRes.ok) {
        throw new Error(`Lookup failed (${lookupRes.status})`);
      }

      const receiver = await lookupRes.json();
      const receiverId = receiver?.id;

      if (!receiverId) {
        throw new Error("Lookup response missing user id.");
      }

      if (Number(receiverId) === Number(userId)) {
        setAddMessage("You can’t send a friend request to yourself.");
        return;
      }

      const requestRes = await fetch(`/api/friend-requests`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          senderId: userId,
          receiverId,
        }),
      });

      if (requestRes.status === 401) {
        localStorage.removeItem("token");
        navigate("/login");
        return;
      }

      if (!requestRes.ok) {
        const body = await requestRes.text();
        setAddMessage(body || "Unable to send friend request.");
        return;
      }

      const result = await requestRes.json();

      setAddUsername("");
      setAddMessage(result.message || "Request Sent");

      await refreshProfile();
    } catch (err) {
      setAddMessage(err?.message || "Unable to send friend request.");
    } finally {
      setAddBusy(false);
    }
  };

  return (
    <main className="main" style={{ paddingTop: "40px" }}>
      <section className="section">
        <div
          className="community-header"
          style={{
            marginBottom: "30px",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "flex-end",
          }}
        >
          <div>
            <h2 className="section-title">Community</h2>
            <div className="hero-sub" style={{ marginBottom: 0 }}>
              {profile?.userName ? `Welcome, ${profile.userName}` : "Your friends list and presence"}
            </div>
          </div>

          <div className="stat-val" style={{ fontSize: "1.2rem" }}>
            {friends.length} Friends
          </div>
        </div>

        {!loading && !error && (
          <div className="game-card" style={{ padding: "24px", marginBottom: "40px" }}>
            <h3 className="game-card-title" style={{ marginBottom: "16px" }}>
              Send Friend Request
            </h3>

            <form onSubmit={handleAddFriend} style={{ display: "flex", gap: "12px" }}>
              <input
                className="form-input"
                type="text"
                placeholder="Enter username"
                style={{ flex: 1 }}
                value={addUsername}
                onChange={(ev) => setAddUsername(ev.target.value)}
                disabled={addBusy}
              />

              <button className="btn btn-red" type="submit" disabled={addBusy}>
                {addBusy ? "Sending..." : "Send Request"}
              </button>
            </form>

            {addMessage && (
              <div
                style={{
                  marginTop: "12px",
                  fontSize: "0.9rem",
                  color:
                    addMessage.includes("Request Sent") || addMessage.includes("sent")
                      ? "var(--blue)"
                      : "var(--red)",
                }}
              >
                {addMessage}
              </div>
            )}
          </div>
        )}

        {loading && <div className="section-status">LOADING...</div>}

        {!loading && error && <div className="section-status error">{error}</div>}

        {!loading && !error && friends.length === 0 && (
          <div className="hero-sub">
            No friends yet. Send a friend request to connect with another user.
          </div>
        )}

        {!loading && !error && friends.length > 0 && (
          <div className="games-grid">
            {friends.map((f) => {
              const { label, tone } = normalizeStatus(f?.status);

              return (
                <div key={f?.id ?? `${f?.userName}-${label}`} className="game-card">
                  <div className="game-card-info" style={{ alignItems: "center" }}>
                    <div style={{ display: "flex", alignItems: "center", gap: "15px" }}>
                      <div className="nav-avatar" style={{ width: "40px", height: "40px" }}>
                        {String(f?.userName || "?").slice(0, 1).toUpperCase()}
                      </div>

                      <div>
                        <div className="game-card-title">{f?.userName || "Unknown user"}</div>
                        <div style={{ fontSize: "0.8rem", color: "var(--text-muted)" }}>
                          {f?.email || ""}
                        </div>
                      </div>
                    </div>

                    <div
                      style={{
                        padding: "4px 12px",
                        borderRadius: "100px",
                        fontSize: "0.75rem",
                        fontWeight: 700,
                        textTransform: "uppercase",
                        letterSpacing: "0.05em",
                        background:
                          tone === "online"
                            ? "rgba(0, 255, 127, 0.1)"
                            : "rgba(255, 255, 255, 0.05)",
                        color: tone === "online" ? "#00ff7f" : "var(--text-muted)",
                        border: `1px solid ${
                          tone === "online"
                            ? "rgba(0, 255, 127, 0.2)"
                            : "rgba(255, 255, 255, 0.1)"
                        }`,
                      }}
                    >
                      {label}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </section>
    </main>
  );
}