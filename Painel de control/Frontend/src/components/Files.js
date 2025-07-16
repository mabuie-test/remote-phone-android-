import React, { useEffect, useState } from 'react';
import API from '../api';

export default function Files() {
  const [files, setFiles] = useState([]);

  useEffect(() => {
    const deviceId = prompt('Insira o seu deviceId:');
    API.get(`/devices/${deviceId}/uploads`)
      .then(res => setFiles(res.data))
      .catch(console.error);
  }, []);

  return (
    <div>
      <h2>Ficheiros Recebidos</h2>
      <ul>
        {files.map(f => (
          <li key={f.filename}>
            <a href={f.url} target="_blank" rel="noreferrer">
              {f.filename}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}
