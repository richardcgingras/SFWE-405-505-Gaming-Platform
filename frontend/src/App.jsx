import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home/Home.jsx'
import Login from './pages/Login/Login.jsx'
import Signup from './pages/Signup/Signup.jsx'
import ChatPage from './pages/ChatPage/ChatPage.jsx'
import Community from './pages/Community/Community.jsx'
import UserProfilePage from './pages/UserProfile/UserProfile.jsx'
import VideoGame from './pages/VideoGame/VideoGame.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/chat" element={<ChatPage />} />
      <Route path="/community" element={<Community />} />
      <Route path="/profiles/:id" element={<UserProfilePage />} />
      <Route path="/videogame/:id" element={<VideoGame />} />
    </Routes>
  )
}