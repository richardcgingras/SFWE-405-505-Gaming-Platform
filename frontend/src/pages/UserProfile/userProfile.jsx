import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { getUserProfile, updateBio } from "../../services/UserProfile.js";
import "./userProfile.css";

export default function UserProfile() {
  const { id } = useParams();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editingBio, setEditingBio] = useState(false);
  const [newBio, setNewBio] = useState("");
  const [savingBio, setSavingBio] = useState(false);

  const currentUserId = localStorage.getItem("userId");
  const isOwnProfile = currentUserId === id;

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        setLoading(true);
        const data = await getUserProfile(id);
        setProfile(data);
        setNewBio(data.bio || "");
        setError(null);
      } catch (err) {
        console.error("Error fetching profile:", err);
        setError(err.message || "Could not load user profile");
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchProfile();
    }
  }, [id]);

  const handleSaveBio = async () => {
    try {
      setSavingBio(true);
      await updateBio(id, newBio);
      setProfile({ ...profile, bio: newBio });
      setEditingBio(false);
    } catch (err) {
      console.error("Failed to save bio:", err);
      alert(err.message || "Failed to save bio");
    } finally {
      setSavingBio(false);
    }
  };

  if (loading) {
    return (
      <main className="main" style={{ paddingTop: '40px' }}>
        <div className="section-status">
          <p>Loading profile...</p>
        </div>
      </main>
    );
  }

  if (error || !profile) {
    return (
      <main className="main" style={{ paddingTop: '40px' }}>
        <div className="section-status error">
          <p>{error || "User profile not found"}</p>
          <Link to="/home" className="btn btn-ghost">Return Home</Link>
        </div>
      </main>
    );
  }

  return (
    <main className="main profile-page" style={{ paddingTop: '40px' }}>
      {/* Header Section: Username and Status */}
      <header className="profile-header">
        <div className="profile-main-info">
          <h1 className="profile-username">{profile.userName}</h1>
          <div className="profile-status-badge">
            <span className={`status-dot ${profile.status}`}></span>
            {profile.status}
          </div>
        </div>
        
        <div className="profile-contact-bio">
          <p className="profile-email">{profile.email}</p>
          <div className="profile-bio-box">
            <div className="profile-section-header-mini">
              <h3 className="profile-section-mini-title">About Me</h3>
              {isOwnProfile && !editingBio && (
                <button 
                  className="btn-link-small" 
                  onClick={() => setEditingBio(true)}
                >
                  Edit Bio
                </button>
              )}
            </div>
            
            {editingBio ? (
              <div className="bio-edit-container">
                <textarea
                  className="bio-textarea"
                  value={newBio}
                  onChange={(e) => setNewBio(e.target.value)}
                  placeholder="Tell us about yourself..."
                  maxLength={500}
                />
                <div className="bio-edit-actions">
                  <button 
                    className="btn btn-red btn-small" 
                    onClick={handleSaveBio}
                    disabled={savingBio}
                  >
                    {savingBio ? "Saving..." : "Save"}
                  </button>
                  <button 
                    className="btn btn-ghost btn-small" 
                    onClick={() => {
                      setEditingBio(false);
                      setNewBio(profile.bio || "");
                    }}
                    disabled={savingBio}
                  >
                    Cancel
                  </button>
                </div>
              </div>
            ) : (
              <p className="profile-bio-text">{profile.bio || "No bio provided."}</p>
            )}
          </div>
        </div>
      </header>

      {/* Categories Section */}
      <section className="section">
        <div className="section-header">
          <h2 className="section-title">Preferred Categories</h2>
        </div>
        <div className="profile-categories">
          {profile.preferredCategories && profile.preferredCategories.length > 0 ? (
            <div className="category-tags">
              {profile.preferredCategories.map((cat) => (
                <span key={cat.id || cat.type} className="category-tag">
                  {cat.type || cat.name || cat}
                </span>
              ))}
            </div>
          ) : (
            <p className="empty-msg">No preferred categories selected.</p>
          )}
        </div>
      </section>

      {/* Library Section */}
      <section className="section">
        <div className="section-header">
          <h2 className="section-title">Game Library</h2>
          <Link to="/library" className="section-link">View Full Library →</Link>
        </div>
        <div className="profile-game-grid">
          {profile.gameLibrary && profile.gameLibrary.length > 0 ? (
            profile.gameLibrary.map((game) => (
              <div key={game.id} className="profile-game-card">
                <div className="game-card-info">
                  <h3 className="game-card-title">{game.name || game.title}</h3>
                </div>
              </div>
            ))
          ) : (
            <p className="empty-msg">Library is empty.</p>
          )}
        </div>
      </section>

      {/* Wishlist Section */}
      <section className="section">
        <div className="section-header">
          <h2 className="section-title">Wishlist</h2>
          <Link to="/wishlist" className="section-link">View Wishlist →</Link>
        </div>
        <div className="profile-wishlist-grid">
          {profile.wishList && profile.wishList.games && profile.wishList.games.length > 0 ? (
            profile.wishList.games.map((game) => (
              <div key={game.id} className="profile-game-card wishlist-card">
                <div className="game-card-info">
                  <h3 className="game-card-title">{game.name || game.title}</h3>
                  <span className="game-price">${game.price.toFixed(2)}</span>
                </div>
              </div>
            ))
          ) : (
            <p className="empty-msg">Wishlist is empty.</p>
          )}
        </div>
      </section>

      {/* Friends Section */}
      <section className="section">
        <div className="section-header">
          <h2 className="section-title">Friends</h2>
        </div>
        <div className="friends-list">
          {profile.friends && profile.friends.length > 0 ? (
            <div className="friends-grid">
              {profile.friends.map((friend) => (
                <Link to={`/profile/${friend.id}`} key={friend.id} className="friend-card">
                  <div className="friend-avatar">
                    {friend.userName?.[0]?.toUpperCase()}
                  </div>
                  <div className="friend-info">
                    <span className="friend-name">{friend.userName}</span>
                    <span className="friend-status">{friend.status}</span>
                  </div>
                </Link>
              ))}
            </div>
          ) : (
            <p className="empty-msg">No friends added yet.</p>
          )}
        </div>
      </section>
    </main>
  );
}
