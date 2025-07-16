const router = require('express').Router();
const User   = require('../models/User');
const jwt    = require('jsonwebtoken');
const auth   = require('../middleware/auth');
const roles  = require('../middleware/roles');

// Registrar (só admin)
router.post(
  '/register',
  auth,
  roles(['admin']),
  async (req, res) => {
    const { username, password, role } = req.body;
    const u = new User({ username, password, role });
    await u.save();
    res.json({ msg: 'User criado' });
  }
);

// Login
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
//retrancado
