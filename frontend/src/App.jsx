import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home/Home.jsx'
import Login from './pages/Login/Login.jsx'
import Signup from './pages/Signup/Signup.jsx'
import ResetPassword from './pages/ResetPassword/ResetPassword.jsx'
import ChatPage from './pages/ChatPage/ChatPage.jsx'
import Library from './pages/Library/Library.jsx'
import Download from './pages/Download/Download.jsx'
import VideoGames from './pages/VideoGame/VideoGame.jsx'
import Community from './pages/Community/Community.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/reset-password" element={<ResetPassword />} />
      <Route path="/chat" element={<ChatPage />} />
      <Route path="/library" element={<Library />} />
      <Route path="/download" element={<Download />} />
      <Route path="/games" element={<VideoGames />} />
      <Route path="/community" element={<Community />} />
    </Routes>
  )
}