const router = require('express').Router();
const User   = require('../models/User');
const jwt    = require('jsonwebtoken');
// Removemos temporariamente estes middlewares
// const auth   = require('../middleware/auth');
// const roles  = require('../middleware/roles');

// Registrar (acesso aberto temporariamente)
router.post(
  '/register',
  async (req, res) => {
    try {
      const { username, password, role } = req.body;
      const u = new User({ username, password, role });
      await u.save();
      return res.json({ msg: 'User criado' });
    } catch (err) {
      console.error('Erro ao criar user:', err);
      return res.status(500).json({ msg: 'Erro interno' });
    }
  }
);

// Login (mantém-se igual)
router.post('/login', async (req, res) => {
  const { username, password } = req.body;
  const u = await User.findOne({ username });
  if (!u || !(await u.matchPassword(password)))
    return res.status(400).json({ msg: 'Credenciais inválidas' });
  const token = jwt.sign(
    { id: u._id, role: u.role },
    process.env.JWT_SECRET,
    { expiresIn: process.env.TOKEN_EXPIRY }
  );
  res.json({ token });
});

module.exports = router;
