import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <Link to="/devices">Dispositivos</Link>
      <Link to="/files">Ficheiros</Link>
      <Link to="/users">Utilizadores</Link>
      {token
        ? <button onClick={logout}>Logout</button>
        : <Link to="/login">Login</Link>
      }
    </nav>
  );
}
