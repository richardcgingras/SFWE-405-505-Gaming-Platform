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
    const id = Number(sub);
    return Number.isFinite(id) ? id : null;
  } catch {
    return null;
  }
}

function normalizeStatus(raw) {
  const s = String(raw || "").toLowerCase();
  if (["online", "active"].includes(s)) return { label: "Online", tone: "online" };
  return { label: "Offline", tone: "offline" };
}

export default function Community() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [addUsername, setAddUsername] = useState("");
  const [addMessage, setAddMessage] = useState("");
  const [requests, setRequests] = useState([]);

  const token = useMemo(() => localStorage.getItem("token"), []);
  const userId = useMemo(() => getUserIdFromToken(token), [token]);

  useEffect(() => {
    if (!token || !userId) {
      navigate("/login");
      return;
    }

    loadData();
  }, []);

  const loadData = async () => {
    const profileRes = await fetch(`/api/user-profiles/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    setProfile(await profileRes.json());

    const reqRes = await fetch(`/api/friend-requests/received/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    setRequests(await reqRes.json());
  };

  const sendRequest = async (e) => {
    e.preventDefault();

    const username = addUsername.trim();
    setAddMessage("");

    if (!username) {
      setAddMessage("Enter a username.");
      return;
    }

    const lookup = await fetch(`/api/user-profiles/username/${encodeURIComponent(username)}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!lookup.ok) {
      setAddMessage("User not found.");
      return;
    }

    const receiver = await lookup.json();

    const res = await fetch(`/api/friend-requests`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        senderId: userId,
        receiverId: receiver.id,
      }),
    });

    if (!res.ok) {
      const text = await res.text();
      setAddMessage(text || "Unable to send friend request.");
      return;
    }

    const data = await res.json();
    setAddMessage(data.message || "Request Sent");
    setAddUsername("");
    loadData();
  };

  const handleAction = async (id, action) => {
    await fetch(`/api/friend-requests/${id}/${action}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });

    loadData();
  };

  const friends = profile?.friends || [];

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

        <div className="game-card" style={{ padding: "24px", marginBottom: "30px" }}>
          <h3 className="game-card-title" style={{ marginBottom: "16px" }}>
            Send Friend Request
          </h3>

          <form onSubmit={sendRequest} style={{ display: "flex", gap: "12px" }}>
            <input
              className="form-input"
              type="text"
              placeholder="Enter username"
              style={{ flex: 1 }}
              value={addUsername}
              onChange={(e) => setAddUsername(e.target.value)}
            />

            <button className="btn btn-red" type="submit">
              Send Request
            </button>
          </form>

          {addMessage && (
            <div
              style={{
                marginTop: "12px",
                fontSize: "0.95rem",
                color:
                  addMessage.includes("Request") || addMessage.includes("Sent")
                    ? "var(--blue)"
                    : "var(--red)",
              }}
            >
              {addMessage}
            </div>
          )}
        </div>

        {requests.length > 0 && (
          <div className="game-card" style={{ padding: "24px", marginBottom: "40px" }}>
            <h3 className="game-card-title" style={{ marginBottom: "16px" }}>
              Pending Friend Requests
            </h3>

            {requests.map((r) => (
              <div
                key={r.id}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  padding: "14px 0",
                  borderBottom: "1px solid rgba(255,255,255,0.06)",
                }}
              >
                <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
                  <div className="nav-avatar" style={{ width: "38px", height: "38px" }}>
                    {String(r.sender?.userName || "?").slice(0, 1).toUpperCase()}
                  </div>

                  <div>
                    <div className="game-card-title">
                      {r.sender?.userName || "Unknown user"}
                    </div>
                    <div style={{ fontSize: "0.85rem", color: "var(--text-muted)" }}>
                      wants to be friends
                    </div>
                  </div>
                </div>

                <div style={{ display: "flex", gap: "10px" }}>
                  <button
                    className="btn btn-ghost"
                    onClick={() => handleAction(r.id, "accept")}
                  >
                    Accept
                  </button>

                  <button
                    className="btn btn-red"
                    onClick={() => handleAction(r.id, "deny")}
                  >
                    Deny
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        <h3 className="section-title" style={{ fontSize: "1.3rem", marginBottom: "18px" }}>
          Friends
        </h3>

        {friends.length === 0 && (
          <div className="hero-sub">
            No friends yet. Send or accept a friend request to connect with another user.
          </div>
        )}

        {friends.length > 0 && (
          <div className="games-grid">
            {friends.map((f) => {
              const { label, tone } = normalizeStatus(f.status);

              return (
                <div key={f.id} className="game-card">
                  <div className="game-card-info" style={{ alignItems: "center" }}>
                    <div style={{ display: "flex", alignItems: "center", gap: "15px" }}>
                      <div className="nav-avatar" style={{ width: "40px", height: "40px" }}>
                        {String(f.userName || "?").slice(0, 1).toUpperCase()}
                      </div>

                      <div>
                        <div className="game-card-title">{f.userName}</div>
                        <div style={{ fontSize: "0.8rem", color: "var(--text-muted)" }}>
                          {f.email}
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