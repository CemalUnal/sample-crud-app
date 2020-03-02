package io.github.cemalunal.backend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.cemalunal.backend.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @Autowired
    private BaseTestUtil<Customer> customerTestUtil;

    @DisplayName("Test Create Customer Controller")
    @Test
    public void testCreateCustomerController() throws Exception {
        Customer customer = new Customer();
        customer.setName("test");

        MvcResult mvcResult = customerTestUtil.sendPostAndExpectSuccessful(customer, "/customers");
        String customerResponseAsString = mvcResult.getResponse().getContentAsString();

        Customer savedCustomer = customerTestUtil.objectFromString(customerResponseAsString, Customer.class);

        assertNotNull(savedCustomer.getId());
        assertEquals(customer.getName(), savedCustomer.getName());
    }

    @DisplayName("Test Find All Customers Controller")
    @Test
    public void testFindAllCustomersController() throws Exception {
        Customer customer = new Customer();
        customer.setName("test1");

        Customer customer2 = new Customer();
        customer2.setName("test2");

        MvcResult mvcResult = customerTestUtil.sendPostAndExpectSuccessful(customer, "/customers");
        Customer savedCustomer1 = customerTestUtil.objectFromString(mvcResult.getResponse().getContentAsString(), Customer.class);

        mvcResult = customerTestUtil.sendPostAndExpectSuccessful(customer2, "/customers");
        Customer savedCustomer2 = customerTestUtil.objectFromString(mvcResult.getResponse().getContentAsString(), Customer.class);

        mvcResult = customerTestUtil.sendGetAndExpectOk("/customers");
        String allCustomersResponseAsString = mvcResult.getResponse().getContentAsString();
        List<Customer> customers = listFromString(allCustomersResponseAsString);

        assertTrue(customers.contains(savedCustomer1));
        assertTrue(customers.contains(savedCustomer2));
    }

    @DisplayName("Test Delete Customer")
    @Test
    public void testDeleteCustomerController() throws Exception {
        Customer customer = new Customer();
        customer.setName("test3");

        MvcResult mvcResult = customerTestUtil.sendPostAndExpectSuccessful(customer, "/customers");
        Customer savedCustomer = customerTestUtil.objectFromString(mvcResult.getResponse().getContentAsString(), Customer.class);

        customerTestUtil.sendDeleteAndExpectOk("/customers/{customerId}", savedCustomer.getId());
        customerTestUtil.sendDeleteAndExpectNoContent("/customers/{customerId}", savedCustomer.getId());
    }

    private List<Customer> listFromString(String customerListString) {
        JsonObject jsonObject = customerTestUtil.getGson().fromJson(customerListString, JsonObject.class);
        JsonElement jsonElement = jsonObject.get("data");
        return Arrays.asList(customerTestUtil.getGson().fromJson(jsonElement, Customer[].class));
    }
}