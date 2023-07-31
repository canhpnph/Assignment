const model = require('../../models/user.model');
var fs = require('fs');
const bcrypt = require("bcrypt");

exports.list_user = async (req, res, next) => {
    let check = null;
    var list = null;

    if (typeof (req.query._id) != 'undefined') {
        check = { _id: req.query._id };
    }
    if (typeof (req.query.username) != 'undefined') {
        check = { username: req.query.username };
    }

    if (typeof (req.query.phone) != 'undefined') {
        check = { phone: req.query.phone };
    }

    list = await model.userModel.find(check);
    if (list.length == 0) {
        return res.status(303).json({ message: "Không có dữ liệu!" })
    }
    return res.status(200).json(list);
};

exports.checkUsername = async (req, res, next) => {
    const { username } = req.body;

    const check_user = await model.userModel.findOne({ username })
    let isValid = true;
    let message = "";

    if (check_user) {
        isValid = false;
    }

    if (isValid) {
        res.status(200).json({ message: "Không tồn tại" });
    } else {
        res.status(210).json({ message: "Đã tồn tại username " + username });
    }
}


exports.registerUser = async (req, res, next) => {
    try {
        const user = new model.userModel(req.body);
        await user.save();
        const token = await user.generateAuthToken();
        return res.status(201).send({ user, token, message: 'Đăng ký thành công!' });
    } catch (error) {
        return res.status(400).send(error);
    }

}

exports.login = async (req, res, next) => {
    try {
        const { username, password } = req.body;
        const user = await model.userModel.findByCredentials(username, password);
        if (!user) {
            return res.status(401).send({ error: 'Đăng nhập thất bại!' })
        }
        const token = await user.generateAuthToken()
        req.session.userLogin = user
        res.status(200).json(user);

    } catch (error) {
        res.status(400).json({ message: "Đăng nhập không thành công. Username hoặc password sai!" })
    }

};

exports.updateInfoUser = async (req, res, next) => {
    let id_user = req.params.id;
    let random_char = randomString(5);

    if (req.method == 'PUT') {
        if (req.file != null) {
            fs.renameSync(req.file.path, './public/uploads/' + req.file.originalname);
            let obj = new model.userModel();
            obj._id = id_user;
            obj.fullname = req.body.fullname;
            obj.date =  req.body.date;
            obj.image = req.file.originalname;

            try {
                await model.userModel.findByIdAndUpdate(id_user, obj);
                return res.status(200).json({ user: obj })

            } catch (error) {
                res.status(500).send({ message: error.message });
                console.log(error);
            }
        } else {
            let obj = new model.userModel();
            obj._id = id_user;
            obj.fullname = req.body.fullname;
            obj.date =  req.body.date;

            try {
                await model.userModel.findByIdAndUpdate(id_user, obj);
                return res.status(200).json({ user: obj })

            } catch (error) {
                res.status(500).send({ message: error.message });
                console.log(error);
            }
        }

    }
}

exports.checkOldPassword = async (req, res, next) => {
    const { _id, password } = req.body;
    const user = await model.userModel.findOne({ _id });
    const isPasswordMatch = await bcrypt.compare(password, user.password);

    if (isPasswordMatch) {
        return res.status(200).json({message: "Mật khẩu cũ khớp"});
    } else {
        return res.json({message: "Mật khẩu cũ không khớp"});
    }
}

exports.updatePasswordUser = async (req, res, next) => {
    let id_user = req.params.id;

    if (req.method == 'PUT') {
        let obj = new model.userModel();
        obj._id = id_user;
        const salt = await bcrypt.genSalt(10);
        obj.password = await bcrypt.hash(req.body.password, salt);

        try {
            await model.userModel.findByIdAndUpdate(id_user, obj);
            return res.status(200).json(obj);
        } catch (error) {
            console.log(error);
            return res.status(500).send({ message: error.message });
        }
    }
}

exports.logout = async (req, res, next) => {
    try {
        req.user.token = null; //xóa token
        await req.user.save()
        return res.status(200).json({ msg: 'Đăng xuất thành công' });
    } catch (error) {
        console.log(error);
        res.status(500).send(error.message)
    }
}

exports.profile = async (req, res, next) => {
    try {
        return res.status(200).json(req.user);
    } catch (error) {
        return res.status(500).json({ error: error.message })
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