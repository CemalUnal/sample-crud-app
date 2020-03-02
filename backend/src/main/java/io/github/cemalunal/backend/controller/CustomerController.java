package io.github.cemalunal.backend.controller;

import io.github.cemalunal.backend.aspect.LogExecutionTime;
import io.github.cemalunal.backend.model.Customer;
import io.github.cemalunal.backend.response.Response;
import io.github.cemalunal.backend.response.ResponseUtil;
import io.github.cemalunal.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ResponseUtil responseUtil;

    @Autowired
    public CustomerController(CustomerService customerService, ResponseUtil responseUtil) {
        this.customerService = customerService;
        this.responseUtil = responseUtil;
    }

    @GetMapping
    @LogExecutionTime
    public ResponseEntity<Response<List<Customer>>> getAllCustomers() {
        List<Customer> customers = customerService.getCustomers();
        Response<List<Customer>> response = responseUtil.createResponse("Successfully fetched all customers", customers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @LogExecutionTime
    public ResponseEntity<Response<Customer>> saveCustomer(@RequestBody Customer customerRequest) {
        Customer customer = customerService.saveCustomer(customerRequest);
        Response<Customer> response = responseUtil.createResponse("New customer " + customer.getName() + " saved to database", customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{customerId}")
    @LogExecutionTime
    public ResponseEntity<Response<String>> deleteCustomer(@PathVariable("customerId") String customerId) {
        Customer customer = customerService.findById(customerId);
        Response<String> response;

        if (customer == null) {
            response = responseUtil.createResponse("Customer not found with id " + customerId, customerId);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        customerService.deleteCustomer(customerId);
        response = responseUtil.createResponse("Customer " + customerId + " deleted", customerId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
