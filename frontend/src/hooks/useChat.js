import { useState, useEffect, useRef, useCallback } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const SOCKET_URL = "http://localhost:8080/ws";

export function useChat(currentUserId, recipientId) {
  const [messages, setMessages] = useState([]);
  const [connected, setConnected] = useState(false);
  const clientRef = useRef(null);

  useEffect(() => {
    if (!currentUserId || !recipientId) return;
    fetch(`/api/messages/${currentUserId}/${recipientId}`)
      .then((res) => res.json())
      .then(setMessages)
      .catch(console.error);
  }, [currentUserId, recipientId]);

  useEffect(() => {
    if (!currentUserId) return;
    const client = new Client({
      webSocketFactory: () => new SockJS(SOCKET_URL),
      reconnectDelay: 5000,
      onConnect: () => {
        setConnected(true);
        client.subscribe(`/user/${currentUserId}/queue/messages`, (msg) => {
          setMessages((prev) => [...prev, JSON.parse(msg.body)]);
        });
      },
      onDisconnect: () => setConnected(false),
    });
    client.activate();
    clientRef.current = client;
    return () => client.deactivate();
  }, [currentUserId]);

  const sendMessage = useCallback((content) => {
    if (!clientRef.current?.connected || !content.trim()) return;
    const message = { senderId: currentUserId, recipientId, content: content.trim() };
    setMessages((prev) => [
      ...prev,
      { ...message, timestamp: new Date().toISOString(), status: "SENT" },
    ]);
    clientRef.current.publish({
      destination: "/app/chat",
      body: JSON.stringify(message),
    });
  }, [currentUserId, recipientId]);

  return { messages, sendMessage, connected };
}