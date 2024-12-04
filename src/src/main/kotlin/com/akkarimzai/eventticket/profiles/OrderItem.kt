package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.OrderItem
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand


fun UpdateOrderItemCommand.toOrderItem(orderItem: OrderItem
): OrderItem = OrderItem(
        id = orderItem.id,
        order = orderItem.order,
        ticket = orderItem.ticket,
        amount = this.amount)