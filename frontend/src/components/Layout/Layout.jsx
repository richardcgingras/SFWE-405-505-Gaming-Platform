import React from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import Navbar from '../Navbar/Navbar';
import Footer from '../Footer/Footer';

const Layout = () => {
  const location = useLocation();
  const isHomePage = location.pathname === '/home';

  return (
    <div className="page">
      <div className="bg-glow" />
      <Navbar />
      <div className="main-content">
        <Outlet />
      </div>
      <Footer />
    </div>
  );
};

export default Layout;
