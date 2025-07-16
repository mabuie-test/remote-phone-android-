import React, { useEffect, useState } from 'react';
import API from '../api';

export default function Devices() {
  const [devices, setDevices] = useState([]);
  const [name, setName] = useState('');

  useEffect(() => {
    API.get('/devices')
      .then(res => setDevices(res.data))
      .catch(console.error);
  }, []);

  const addDevice = async e => {
    e.preventDefault();
    const res = await API.post('/devices', { name });
    setDevices([...devices, res.data]);
    setName('');
  };

  return (
    <div>
      <h2>Dispositivos</h2>
      <form onSubmit={addDevice}>
        <input
          placeholder="Nome do dispositivo"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <button type="submit">Adicionar</button>
      </form>
      <ul>
        {devices.map(d => (
          <li key={d._id}>
            {d.name} (Último contacto: {new Date(d.lastSeen).toLocaleString()})
          </li>
        ))}
      </ul>
    </div>
  );
}
