const db = require('../db_connection');

const loverTypeSchema = new db.mongoose.Schema ({
    name: {type: String, required: true},
    description: {type: String, required: false},
}, {collection: 'tb_loverType'}
);

let loverTypeModel = db.mongoose.model('loverTypeModel', loverTypeSchema);

module.exports = loverTypeModel