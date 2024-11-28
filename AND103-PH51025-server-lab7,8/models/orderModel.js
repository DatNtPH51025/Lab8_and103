const mongoose = require('mongoose');

// Schema đơn hàng
const orderSchema = new mongoose.Schema({
    user_id: { 
        type: mongoose.Schema.Types.ObjectId, 
        ref: 'User' // Liên kết với bảng User
    },
    location: String, // Địa chỉ chi tiết
    province: String, // Thành phố
    district: String, // Quận
    ward: String, // Phường
    orderStatus: { 
        type: String, 
        default: 'Pending' // Trạng thái đơn hàng, mặc định là "Pending"
    },
    created_at: { 
        type: Date, 
        default: Date.now // Thời gian tạo đơn hàng
    }
});

// Tạo model từ schema
const Order = mongoose.model('Order', orderSchema);
module.exports = Order;



