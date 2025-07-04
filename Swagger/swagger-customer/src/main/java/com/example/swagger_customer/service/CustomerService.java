package com.example.swagger_customer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.swagger_customer.dto.CustomerDTO;

@Service
public class CustomerService {

    private final Map<Long, CustomerDTO> customerMap = new HashMap<>();
    private long nextId = 1;

    public CustomerDTO createCustomer(CustomerDTO customer) {
        customer.setId(nextId++);
        customerMap.put(customer.getId(), customer);
        return customer;
    }

    public CustomerDTO getCustomerById(Long id) {
        return customerMap.get(id);
    }

    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO updatedCustomer) {
        if (!customerMap.containsKey(id)) {
            return null;
        }
        updatedCustomer.setId(id);
        customerMap.put(id, updatedCustomer);
        return updatedCustomer;
    }

    public boolean deleteCustomer(Long id) {
        return customerMap.remove(id) != null;
    }
}
