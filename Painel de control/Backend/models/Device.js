const mongoose = require('mongoose');

const DeviceSchema = new mongoose.Schema({
  owner:     { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  name:      { type: String, required: true },
  lastSeen:  { type: Date, default: Date.now },
  locations: [{ latitude: Number, longitude: Number, at: Date }],
  photos:    [{ filename: String, path: String, uploaded: Date }],
  sms:       [{ sender: String, body: String, timestamp: Date }],
  contacts:  [{ name: String, number: String }],
  calllog:   [{ number: String, type: Number, date: Date, duration: Number }],
  whatsapp:  [{ sender: String, message: String, timestamp: Date }]
});

module.exports = mongoose.model('Device', DeviceSchema);
