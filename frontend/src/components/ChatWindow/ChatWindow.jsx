import { useState, useEffect, useRef } from "react";
import { useChat } from "./hooks/useChat";
import "./ChatWindow.css";

export default function ChatWindow({ currentUserId, recipient, onClose }) {
  const [inputValue, setInputValue] = useState("");
  const bottomRef = useRef(null);
  const { messages, sendMessage, connected } = useChat(currentUserId, recipient.id);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = () => {
    if (!inputValue.trim()) return;
    sendMessage(inputValue);
    setInputValue("");
  };

  const formatTime = (ts) =>
    ts ? new Date(ts).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }) : "";

  return (
    <div className="chat-window">
      <div className="chat-header">
        <div className="chat-header-info">
          <div className="chat-avatar">
            {recipient.avatarUrl
              ? <img src={recipient.avatarUrl} alt={recipient.username} />
              : <span>{recipient.username?.[0]?.toUpperCase()}</span>}
          </div>
          <div>
            <div className="chat-username">{recipient.username}</div>
            <div className={`chat-status ${connected ? "online" : "offline"}`}>
              {connected ? "Online" : "Connecting..."}
            </div>
          </div>
        </div>
        <button className="chat-close-btn" onClick={onClose}>✕</button>
      </div>

      <div className="chat-messages">
        {messages.length === 0 && (
          <div className="chat-empty">No messages yet. Say hello!</div>
        )}
        {messages.map((msg, idx) => {
          const isMine = msg.senderId === currentUserId;
          return (
            <div key={msg.id || idx} className={`chat-bubble-wrapper ${isMine ? "mine" : "theirs"}`}>
              <div className={`chat-bubble ${isMine ? "mine" : "theirs"}`}>
                <span className="chat-bubble-text">{msg.content}</span>
                <span className="chat-bubble-meta">
                  {formatTime(msg.timestamp)}
                  {isMine && (
                    <span className="chat-status-icon">
                      {msg.status === "DELIVERED" || msg.status === "READ" ? " ✓✓" : " ✓"}
                    </span>
                  )}
                </span>
              </div>
            </div>
          );
        })}
        <div ref={bottomRef} />
      </div>

      <div className="chat-input-area">
        <textarea
          className="chat-input"
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
          className="chat-send-btn"
          onClick={handleSend}
          disabled={!inputValue.trim() || !connected}
        >
          Send
        </button>
      </div>
    </div>
  );
}