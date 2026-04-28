import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home/Home.jsx'
import LandingPage from './pages/LandingPage/LandingPage.jsx'
import Login from './pages/Login/Login.jsx'
import Signup from './pages/Signup/Signup.jsx'
import ChatPage from './pages/ChatPage/ChatPage.jsx'
import Library from './pages/Library/Library.jsx'
import Download from './pages/Download/Download.jsx'
import VideoGames from './pages/VideoGame/VideoGame.jsx'
import Community from './pages/Community/Community.jsx'
import Layout from './components/Layout/Layout.jsx'

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<LandingPage />} />
        <Route path="/home" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/chat" element={<ChatPage />} />
        <Route path="/library" element={<Library />} />
        <Route path="/download" element={<Download />} />
        <Route path="/games" element={<VideoGames />} />
        <Route path="/community" element={<Community />} />
      </Route>
    </Routes>
  )
}