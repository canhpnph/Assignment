const model = require('../../models/loverManager.model');
var fs = require('fs');


// lover
exports.list_Lover = async (req, res, next) => {
    let check = null;
    if (typeof (req.query.loverOf) != 'undefined' && req.query.loverOf !== 'all' ) {
        check = { loverOf: req.query.loverOf};
    }
    
    if (typeof (req.query._id) != 'undefined' && req.query._id !== 'all') {
        check = { _id: req.query._id };
    }

    var list = await model.loverModel.find(check).populate('type').populate('loverOf');

    if (list.length == 0) {
        return res.json({ message: 'Không có dữ liệu!' })
    }
    return res.status(200).json(list);
};

exports.add_Lover = async (req, res, next) => {
    let message = '';
    let random_char = randomString(5);

    try {
        fs.renameSync(req.file.path, './public/uploads/' +  req.file.originalname);
        const obj = new model.loverModel();
        obj.name = req.body.name;
        obj.phone = req.body.phone;
        obj.age = req.body.age;
        obj.weight = req.body.weight;
        obj.image = req.file.originalname;
        obj.height = req.body.height;
        obj.type = req.body.type;
        obj.loverOf = req.body.loverOf;
        obj.about = req.body.about;

        await obj.save();
        res.status(201).json({ lover: "Đã thêm người yêu mới. Info của người yêu mới là: " + obj });
    } catch (error) {
        message = 'Thêm thất bại. Lỗi do: ' + error.message;
        res.send(message);
    }

};

exports.updateLover = async (req, res, next) => {
    let id_lover = req.params.id;
    let random_char = randomString(5);

    if (req.method == 'PUT') {
        if (req.file != null) {
            fs.renameSync(req.file.path, './public/uploads/' +  req.file.originalname);
            let obj = new model.loverModel();
            obj._id = id_lover;
            obj.name = req.body.name;
            obj.age = req.body.age;
            obj.weight = req.body.weight;
            obj.phone = req.body.phone;
            obj.image = req.file.originalname;
            obj.height = req.body.height;
            obj.type = req.body.type;
            obj.about = req.body.about;

            try {
                await model.loverModel.findByIdAndUpdate(id_lover, obj);
                return res.status(200).json({ lover: obj, message: 'Sửa thành công!' })
            } catch (error) {
                res.status(500).send({ message: error.message });
                console.log(error);
            }
        } else {
            let obj = new model.loverModel();
            obj._id = id_lover;
            obj.name = req.body.name;
            obj.age = req.body.age;
            obj.weight = req.body.weight;
            obj.phone = req.body.phone;
            obj.height = req.body.height;
            obj.type = req.body.type;
            obj.about = req.body.about;

            try {
                await model.loverModel.findByIdAndUpdate(id_lover, obj);
                return res.status(200).json({ lover: obj, message: 'Sửa thành công!' })
            } catch (error) {
                res.status(500).send({ message: error.message });
                console.log(error);
            }
        }

    }
}

exports.deleteLover = async (req, res, next) => {
    let id_lover = req.params.id;

    if (req.method == 'DELETE') {
        let obj = new model.loverModel(req.body);
        obj._id = id_lover;

        try {
            await model.loverModel.findByIdAndDelete(id_lover);
            return res.status(200).json({ message: "Xoá thành công!" });
        } catch (error) {
            res.status(500).send({ message: error.message });
            console.log(error);
        }
    }
}


// lover type 

exports.listOfLoverType = async (req, res, next) => {
    var typeList = await model.loverTypeModel.find()
    res.send(typeList);
};

exports.add_LoverType = async (req, res, next) => {
    let message = '';
    try {
        const lovertType = new model.loverTypeModel(req.body);
        await lovertType.save();
        res.status(201).json({ message: "Thêm thành công!" });
    } catch (error) {
        message = 'Thêm thất bại. Lỗi do: ' + error.message;
        res.json(message);
    }
};

exports.updateLoverType = async (req, res, next) => {
    let id_loverType = req.params.id;

    if (req.method == 'PUT') {
        let obj = new model.loverTypeModel(req.body);
        obj._id = id_loverType;

        try {
            await model.loverTypeModel.findByIdAndUpdate(id_loverType, obj);
            return res.status(200).json({ loverType: obj, message: 'Sửa thành công!' })
        } catch (error) {
            res.status(500).send({ message: error.message });
            console.log(error);
        }
    }
}

exports.deleteLoverType = async (req, res, next) => {
    let id_loverType = req.params.id;

    if (req.method == 'DELETE') {
        let obj = new model.loverTypeModel(req.body);
        obj._id = id_loverType;

        try {
            await model.loverTypeModel.findByIdAndDelete(id_loverType);
            return res.status(200).json({ message: "Xoá thành công!" })
        } catch (error) {
            res.status(500).send({ message: error.message });
            console.log(error);
        }
    }
}

function randomString(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
        counter += 1;
    }
    return result;
}
