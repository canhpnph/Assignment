const model = require("../../models/message.model");

exports.listMessage = async (req, res, next) => {
    let listMessage = await model.find();
    if (list.length == 0) {
        return res.status(303).json({ message: "Không có dữ liệu!" })
    }
    return res.status(200).json(listMessage);
}

exports.addNewMessage = async (req, res, next) => {
    try {
        const model = new model(req.body);
        await model.save();
        return res.status(201).json(model);
    } catch (error) {
        return res.status(400).json(error);
    }

}