import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Login   from './components/Login';
import Navbar  from './components/Navbar';
import Users   from './components/Users';
import Devices from './components/Devices';
import Files   from './components/Files';

function RequireAuth({ children }) {
  return localStorage.getItem('token')
    ? children
    : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <>
      <Navbar />
      <div className="container">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/users"  element={
            <RequireAuth><Users /></RequireAuth>
          }/>
          <Route path="/devices" element={
            <RequireAuth><Devices /></RequireAuth>
          }/>
          <Route path="/files" element={
            <RequireAuth><Files /></RequireAuth>
          }/>
          <Route path="*" element={<Navigate to="/devices" replace />} />
        </Routes>
      </div>
    </>
  );
}

