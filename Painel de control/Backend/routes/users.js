const router = require('express').Router();
const User   = require('../models/User');
const auth   = require('../middleware/auth');
const roles  = require('../middleware/roles');

router.get(
  '/',
  auth,
  roles(['admin']),
  async (req, res) => {
    const users = await User.find().select('-password');
    res.json(users);
  }
);

module.exports = router;
