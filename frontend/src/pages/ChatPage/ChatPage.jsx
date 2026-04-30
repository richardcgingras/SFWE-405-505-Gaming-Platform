import { useState, useEffect, useRef, useMemo } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useChat } from "../../hooks/useChat.js";
import "./ChatPage.css";

function getUserIdFromToken(token) {
  try {
    const parts = token.split(".");
    if (parts.length < 2) return null;
    const normalized = parts[1].replace(/-/g, "+").replace(/_/g, "/");
    const padLength = (4 - (normalized.length % 4)) % 4;
    const padded = normalized + "=".repeat(padLength);
    const payload = JSON.parse(atob(padded));
    return payload?.sub ?? null;
  } catch {
    return null;
  }
}

export default function ChatPage() {
  const { friendId } = useParams();
  const navigate = useNavigate();
  const token = useMemo(() => localStorage.getItem("token"), []);
  const currentUserId = useMemo(() => getUserIdFromToken(token), [token]);

  const [friendProfile, setFriendProfile] = useState(null);
  const [friendsList, setFriendsList] = useState([]);
  const [inputValue, setInputValue] = useState("");
  const bottomRef = useRef(null);

  // If a friendId is provided, use the chat hook for that friend
  const { messages, sendMessage, connected } = useChat(friendId || null);

  // Redirect to login if no token
  useEffect(() => {
    if (!token || !currentUserId) {
      navigate("/login");
    }
  }, [token, currentUserId, navigate]);

  // Load friend profile when friendId is provided
  useEffect(() => {
    if (!friendId || !token) return;
    fetch(`/api/user-profiles/${friendId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => {
        if (data) setFriendProfile(data);
      })
      .catch(console.error);
  }, [friendId, token]);

  // Load friends list for the sidebar when no friendId is provided
  useEffect(() => {
    if (!currentUserId || !token) return;
    fetch(`/api/user-profiles/${currentUserId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => {
        if (data?.friends) setFriendsList(data.friends);
      })
      .catch(console.error);
  }, [currentUserId, token]);

  // Scroll to bottom on new messages
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = () => {
    if (!inputValue.trim()) return;
    sendMessage(inputValue);
    setInputValue("");
  };

  const formatTime = (ts) =>
    ts
      ? new Date(ts).toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        })
      : "";

  // ── If no friendId, show a friend-picker list ──
  if (!friendId) {
    return (
      <div className="chat-page">
        <div className="chat-page-header">
          <Link to="/community" className="chat-back-btn">
            Back
          </Link>
          <h1 className="chat-page-title">Messages</h1>
        </div>

        <div className="chat-container" style={{ padding: "24px" }}>
          {friendsList.length === 0 ? (
            <div className="chat-empty-state">
              <p>No friends yet. Add friends from the Community page to start chatting.</p>
            </div>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
              {friendsList.map((f) => (
                <button
                  key={f.id}
                  className="chat-friend-row"
                  onClick={() => navigate(`/chat/${f.id}`)}
                >
                  <div className="chat-top-avatar">
                    {String(f.userName || "?").slice(0, 1).toUpperCase()}
                  </div>
                  <div style={{ flex: 1, textAlign: "left" }}>
                    <div className="chat-top-name">{f.userName}</div>
                    <div style={{ fontSize: "0.8rem", color: "var(--text-muted)" }}>
                      {f.email}
                    </div>
                  </div>
                  <span
                    className="btn btn-ghost"
                    style={{ fontSize: "0.8rem", padding: "6px 16px" }}
                  >
                    Chat
                  </span>
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    );
  }

  // ── friendId provided: show the chat window ──
  const recipientName = friendProfile?.userName || "Loading...";

  return (
    <div className="chat-page">
      <div className="chat-page-header">
        <Link to="/community" className="chat-back-btn">
          Back
        </Link>
        <h1 className="chat-page-title">
          Chat with{" "}
          <span className="chat-page-recipient">{recipientName}</span>
        </h1>
      </div>

      <div className="chat-container">
        {/* Top bar */}
        <div className="chat-top-bar">
          <div className="chat-top-info">
            <div className="chat-top-avatar">
              {String(recipientName || "?").slice(0, 1).toUpperCase()}
            </div>
            <div>
              <div className="chat-top-name">{recipientName}</div>
              <div
                className={`chat-top-status ${connected ? "connected" : "disconnected"}`}
              >
                {connected ? "CONNECTED" : "CONNECTING..."}
              </div>
            </div>
          </div>
        </div>

        {/* Messages */}
        <div className="chat-messages-area">
          {messages.length === 0 && (
            <div className="chat-empty-state">
              <p>No messages yet. Say hello!</p>
            </div>
          )}
          {messages.map((msg, idx) => {
            const isMine = String(msg.senderId) === String(currentUserId);
            return (
              <div
                key={msg.id || idx}
                className={`chat-msg-row ${isMine ? "sent" : "received"}`}
              >
                <div className={`chat-msg-bubble ${isMine ? "sent" : "received"}`}>
                  <span>{msg.content}</span>
                  <span className="chat-msg-time">
                    {formatTime(msg.timestamp)}
                  </span>
                </div>
              </div>
            );
          })}
          <div ref={bottomRef} />
        </div>

        {/* Compose */}
        <div className="chat-compose">
          <textarea
            className="chat-compose-input"
            rows={1}
            placeholder="Type a message..."
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter" && !e.shiftKey) {
                e.preventDefault();
                handleSend();
              }
            }}
          />
          <button
            className="chat-send-button"
            onClick={handleSend}
            disabled={!inputValue.trim() || !connected}
          >
            Send
          </button>
        </div>
      </div>
    </div>
  );
}