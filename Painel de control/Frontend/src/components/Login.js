import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api';

export default function Login() {
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');
  const [err, setErr] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const { data } = await API.post('/auth/login', { username: user, password: pass });
      localStorage.setItem('token', data.token);
      navigate('/devices');
    } catch {
      setErr('Credenciais inválidas');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="form">
      <h2>Login</h2>
      {err && <p className="error">{err}</p>}
      <input
        placeholder="Username"
        value={user}
        onChange={e => setUser(e.target.value)}
      />
      <input
        placeholder="Password"
        type="password"
        value={pass}
        onChange={e => setPass(e.target.value)}
      />
      <button type="submit">Entrar</button>
    </form>
  );
}
