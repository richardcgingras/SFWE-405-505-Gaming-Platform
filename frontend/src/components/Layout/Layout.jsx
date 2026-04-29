import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from '../Navbar/Navbar';
import Footer from '../Footer/Footer';

const Layout = () => {
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
