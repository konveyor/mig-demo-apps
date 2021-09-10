package io.konveyor.demo.orders.repository;

import io.konveyor.demo.orders.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

}
