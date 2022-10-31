package ru.akirakozov.sd.aop.domain;

import ru.akirakozov.sd.aop.dao.CustomerInMemoryDao;

/**
 * @author akirakozov
 */
public class CustomerManagerImpl implements CustomerManager {
    CustomerInMemoryDao customerDao = new CustomerInMemoryDao();

    private void sneakyMethod() {}
    public void sneakyMethodPublic() {}

    public CustomerManagerImpl(CustomerInMemoryDao customerDao) {
        this.customerDao = customerDao;
    }

    public int addCustomer(Customer customer) {
        sneakyMethod();
        sneakyMethodPublic();
        return customerDao.addCustomer(customer);
    }

    public Customer findCustomer(int id) {
        sneakyMethod();
        return customerDao.findCustomer(id);
    }
}
