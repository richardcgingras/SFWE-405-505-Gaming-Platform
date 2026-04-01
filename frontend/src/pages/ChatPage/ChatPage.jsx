import { useState } from "react";
import ChatList from "../ChatList/ChatList.jsx";
import ChatWindow from "../ChatWindow.jsx";

const CURRENT_USER = { id: "user_alice", username: "Alice" };

const MOCK_USERS = [
  { id: "user_bob", username: "Bob" },
  { id: "user_charlie", username: "Charlie" },
];

export default function ChatPage() {
  const [selectedUser, setSelectedUser] = useState(null);

  return (
    <div style={{ display: "flex", height: "100vh", background: "#0f0f23" }}>
      <ChatList
        currentUserId={CURRENT_USER.id}
        users={MOCK_USERS}
        activeUserId={selectedUser?.id}
        onSelectUser={setSelectedUser}
      />
      <div style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center" }}>
        {selectedUser ? (
          <ChatWindow
            currentUserId={CURRENT_USER.id}
            recipient={selectedUser}
            onClose={() => setSelectedUser(null)}
          />
        ) : (
          <p style={{ color: "#555", fontFamily: "sans-serif" }}>
            Select a player to start chatting
          </p>
        )}
      </div>
    </div>
  );
}