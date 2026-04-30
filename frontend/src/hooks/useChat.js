import { useState, useEffect, useRef, useCallback } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const SOCKET_URL = "http://localhost:8080/ws";

const getToken = () => localStorage.getItem("token") || "";

// Decodes the JWT and pulls the user ID from the subject claim
const getCurrentUserId = () => {
  const token = getToken();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.sub; // your backend sets subject to the user ID
  } catch {
    return null;
  }
};

export function useChat(recipientId) {
  const currentUserId = getCurrentUserId();
  const [messages, setMessages] = useState([]);
  const [connected, setConnected] = useState(false);
  const clientRef = useRef(null);

  useEffect(() => {
    if (!currentUserId || !recipientId) return;

    const token = getToken();
    fetch(`/api/messages/${currentUserId}/${recipientId}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) return [];
        return res.json();
      })
      .then(setMessages)
      .catch((err) => {
        console.error("Failed to load messages:", err);
        setMessages([]);
      });
  }, [currentUserId, recipientId]);

  useEffect(() => {
    if (!currentUserId) return;

    const token = getToken();
    const client = new Client({
      webSocketFactory: () => new SockJS(SOCKET_URL),
      reconnectDelay: 5000,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      onConnect: () => {
        setConnected(true);
        // subscribes to THIS user's personal queue only
        client.subscribe(`/user/${currentUserId}/queue/messages`, (msg) => {
          setMessages((prev) => [...prev, JSON.parse(msg.body)]);
        });
      },
      onDisconnect: () => setConnected(false),
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"]);
      },
    });

    client.activate();
    clientRef.current = client;
    return () => client.deactivate();
  }, [currentUserId]);

  const sendMessage = useCallback((content) => {
    if (!clientRef.current?.connected || !content.trim()) return;
    const token = getToken();
    const message = { senderId: currentUserId, recipientId, content: content.trim() };
    setMessages((prev) => [
      ...prev,
      { ...message, timestamp: new Date().toISOString(), status: "SENT" },
    ]);
    clientRef.current.publish({
      destination: "/app/chat",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(message),
    });
  }, [currentUserId, recipientId]);

  return { messages, sendMessage, connected, currentUserId };
}