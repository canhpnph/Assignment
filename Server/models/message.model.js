const db = require('../db_connection');
const userModel = require('./user.model');

const messageSchema = new db.mongoose.Schema({
    _id: { type: String, require: true },
    id_sender: { type: db.mongoose.Schema.Types.ObjectId, ref: 'userModel' },
    message: { type: String, required: true }
}, { collection: 'tb_message' }
);

let messageModel = db.mongoose.model('messageModel', messageSchema);

module.exports = messageModel