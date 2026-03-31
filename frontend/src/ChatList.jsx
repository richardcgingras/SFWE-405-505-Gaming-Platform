import { useState, useEffect } from "react";
import "./ChatList.css";

export default function ChatList({ currentUserId, users = [], onSelectUser, activeUserId }) {
  const [unreadCounts, setUnreadCounts] = useState({});

  useEffect(() => {
    if (!currentUserId || users.length === 0) return;
    const fetchUnread = async () => {
      const counts = {};
      await Promise.all(users.map(async (user) => {
        try {
          const res = await fetch(`/api/messages/${user.id}/${currentUserId}/count`);
          if (res.ok) counts[user.id] = await res.json();
        } catch { counts[user.id] = 0; }
      }));
      setUnreadCounts(counts);
    };
    fetchUnread();
    const interval = setInterval(fetchUnread, 10_000);
    return () => clearInterval(interval);
  }, [currentUserId, users]);

  return (
    <div className="chat-list">
      <div className="chat-list-header">Messages</div>
      <div className="chat-list-items">
        {users.length === 0 && (
          <div className="chat-list-empty">No players found.</div>
        )}
        {users.map((user) => (
          <button
            key={user.id}
            className={`chat-list-item ${activeUserId === user.id ? "active" : ""}`}
            onClick={() => onSelectUser(user)}
          >
            <div className="chat-list-avatar">
              {user.avatarUrl
                ? <img src={user.avatarUrl} alt={user.username} />
                : <span>{user.username?.[0]?.toUpperCase()}</span>}
            </div>
            <span className="chat-list-name">{user.username}</span>
            {unreadCounts[user.id] > 0 && (
              <span className="chat-list-badge">{unreadCounts[user.id]}</span>
            )}
          </button>
        ))}
      </div>
    </div>
  );
}