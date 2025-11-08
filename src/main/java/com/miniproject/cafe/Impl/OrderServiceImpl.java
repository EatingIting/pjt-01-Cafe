package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.OrderMapper;
import com.miniproject.cafe.Service.OrderService;
import com.miniproject.cafe.VO.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // (추가)

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderVO> getAdminOrderList() {
        return orderMapper.findAllAdminOrders();
    }

    //신규 주문
    @Override
    @Transactional
    public OrderVO createOrder(OrderVO order) {
        orderMapper.insertOrder(order);
        return order;
    }

    //주문 업데이트 (완료, 취소)
    @Override
    @Transactional
    public void updateOrderStatus(String status, Long orderId) {
        orderMapper.updateOrderStatus(status, orderId);
    }
}