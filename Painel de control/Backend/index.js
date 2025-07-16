require('dotenv').config();
const express = require('express');
const cors    = require('cors');
const connect = require('./config/db');

const app = express();
app.use(cors(), express.json());
connect();

app.use('/auth',    require('./routes/auth'));
app.use('/users',   require('./routes/users'));
app.use('/devices', require('./routes/deviceData'));

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`API a correr na porta ${PORT}`));
