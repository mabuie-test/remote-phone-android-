import React, { useEffect, useState } from 'react';
import API from '../api';

export default function Users() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    API.get('/users')
      .then(res => setUsers(res.data))
      .catch(console.error);
  }, []);

  return (
    <div>
      <h2>Utilizadores</h2>
      <table>
        <thead><tr><th>Username</th><th>Role</th></tr></thead>
        <tbody>
          {users.map(u => (
            <tr key={u._id}>
              <td>{u.username}</td>
              <td>{u.role}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
