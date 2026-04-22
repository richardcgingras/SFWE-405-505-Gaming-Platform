import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import "../Home/App.css";
import {
  getCurrentUser,
  getUserProfileById,
  getFriendStatus,
  sendFriendRequest,
} from "../../services/FriendRequests";

export default function UserProfilePage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [currentUser, setCurrentUser] = useState(null);
  const [profile, setProfile] = useState(null);
  const [status, setStatus] = useState("NONE");
  const [message, setMessage] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }

    const loadProfile = async () => {
      try {
        const me = await getCurrentUser();
        setCurrentUser(me);

        const user = await getUserProfileById(id);
        setProfile(user);

        const friendStatus = await getFriendStatus(me.id, user.id);
        setStatus(friendStatus.status);
      } catch (err) {
        console.error(err);
        setMessage("Unable to load profile.");
      }
    };

    loadProfile();
  }, [id, navigate]);

  const handleSendRequest = async () => {
    try {
      await sendFriendRequest(currentUser.id, profile.id);
      setStatus("PENDING");
      setMessage("Request Sent");
    } catch (err) {
      setMessage(err.message);
    }
  };

  if (!profile) {
    return <div className="page"><div className="bg-glow" /><p style={{ padding: "40px" }}>Loading...</p></div>;
  }

  return (
    <div className="page">
      <div className="bg-glow" />

      <nav className="nav">
        <div className="nav-logo">
          <span className="logo-text">good<span>Gamers</span></span>
        </div>
        <ul className="nav-links">
          <li><Link to="/">Store</Link></li>
          <li><Link to="/community">Community</Link></li>
          <li><Link to="/chat">Chat</Link></li>
        </ul>
      </nav>

      <main style={{ padding: "60px" }}>
        <div className="game-card" style={{ padding: "30px", maxWidth: "700px" }}>
          <div className="game-card-info" style={{ display: "block" }}>
            <h1 className="game-card-title" style={{ fontSize: "2rem", marginBottom: "8px" }}>
              {profile.userName}
            </h1>
            <p style={{ marginBottom: "8px" }}>{profile.email}</p>
            <p style={{ marginBottom: "16px" }}>{profile.bio || "No bio yet."}</p>
            <p style={{ marginBottom: "16px" }}><strong>Friend Status:</strong> {status}</p>

            {status === "NONE" && (
              <button className="btn btn-red" onClick={handleSendRequest}>
                Send Friend Request
              </button>
            )}

            {status === "PENDING" && (
              <button className="btn btn-ghost" disabled>
                Pending
              </button>
            )}

            {status === "FRIENDS" && (
              <button className="btn btn-ghost" disabled>
                Friends
              </button>
            )}

            {message && <p style={{ marginTop: "16px" }}>{message}</p>}
          </div>
        </div>
      </main>
    </div>
  );
}