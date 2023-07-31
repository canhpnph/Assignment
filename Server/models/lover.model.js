const db = require('../db_connection');
const userModel = require('./user.model');

const loverSchema = new db.mongoose.Schema ({
        name: {type: String, required: true},
        age: {type: String, required: true},
        height: {type: String, required: true},
        weight: {type: String, required: true},
        phone: {type: String, required: true },
        about: {type: String, required: false},
        image: {type: String, required: false},
        type: {type: db.mongoose.Schema.Types.ObjectId, ref: 'loverTypeModel'},
        loverOf : {type: db.mongoose.Schema.Types.ObjectId, ref: 'userModel'}
}, {collection: 'tb_lover'}
);

let loverModel = db.mongoose.model('loverModel', loverSchema);

module.exports = loverModel