package dev.stanislavskyi.shoppingcart.service.order;

import dev.stanislavskyi.shoppingcart.dto.OrderDto;
import dev.stanislavskyi.shoppingcart.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}