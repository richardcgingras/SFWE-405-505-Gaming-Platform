import { useState } from "react";
import ChatList from "../ChatList/ChatList.jsx";

const CURRENT_USER = { id: "user_alice", username: "Alice" };

const MOCK_USERS = [
  { id: "user_bob", username: "Bob" },
  { id: "user_charlie", username: "Charlie" },
];

export default function ChatPage() {
  const [selectedUser, setSelectedUser] = useState(null);

  return (
    <div style={{ display: "flex", height: "calc(100vh - var(--nav-height) - 89px)", background: "var(--blue-deeper)" }}>
      <ChatList
        currentUserId={CURRENT_USER.id}
        users={MOCK_USERS}
        activeUserId={selectedUser?.id}
        onSelectUser={setSelectedUser}
      />
      <div style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center" }}>
        {selectedUser ? (
          <div>Chat with {selectedUser.username} (Window TBD)</div>
        ) : (
          <p className="hero-sub">
            Select a player to start chatting
          </p>
        )}
      </div>
    </div>
  );
}