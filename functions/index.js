const functions = require('firebase-functions');
const admin = require('firebase-admin');

// Khởi tạo Firebase Admin SDK
admin.initializeApp();

// Function gửi thông báo
exports.sendNotification = functions.https.onRequest((req, res) => {
    // Lấy thông tin từ request
    const { token, title, body } = req.body;

    // Kiểm tra nếu thiếu tham số
    if (!token || !title || !body) {
        return res.status(400).send('Missing parameters');
    }

    // Tạo đối tượng thông báo
    const message = {
        notification: {
            title: title,
            body: body,
        },
        token: token,  // Token của thiết bị nhận
    };

    // Gửi thông báo qua Firebase Cloud Messaging (FCM)
    admin.messaging().send(message)
        .then((response) => {
            // Gửi phản hồi thành công
            res.status(200).send("Notification sent successfully: " + response);
        })
        .catch((error) => {
            // Gửi phản hồi thất bại
            res.status(500).send("Error sending notification: " + error);
        });
});