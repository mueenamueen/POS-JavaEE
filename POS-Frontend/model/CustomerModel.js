import CustomerTableDto from "../tableDto/CustomerTableDto.js";

export async function getAll() {
  try {
    const response = await fetch("http://localhost:8080/posSystem/customer");
    const data = await response.json();
    const customerTableList = data.map(
      (customer) =>
        new CustomerTableDto(
          customer.id,
          customer.name,
          customer.address,
          customer.salary
        )
    );
    return customerTableList;
  } catch (error) {
    console.error("getAllCustomers:", error);
  }
}

export async function save(id, name, address, salary) {
  if (id && name && address && salary) {
    const response = await fetch("http://localhost:8080/posSystem/customer", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id: id,
        name: name,
        address: address,
        salary: salary,
      }),
    });
    const text = await response.text();
    return text;
  } else {
    return "Invalid Customer";
  }
}

export async function deleteCus(id) {
  try {
    const response = fetch(
      `http://localhost:8080/posSystem/customer?id=${id}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  } catch (error) {
    console.error("removeCustomer:", error);
  }
}

export async function isExist(id) {
  try {
    const response = await fetch(
      `http://localhost:8080/posSystem/customer?id=${id}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    const text = await response.text();
    if (response.status === 404 || text.includes("Customer not found")) {
      return false;
    } else {
      return true;
    }
  } catch (error) {
    console.error("isExistCustomer:", error);
  }
}

export async function getCus(id) {
  try {
    const response = await fetch (
      `http://localhost:8080/posSystem/customer?id=${id}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    const customer = await response.json();
    return customer;
  } catch (error) {
    console.error("getCus: ",error);
  }
}

export async function update(id, name, address, salary) {
  try {
    const response = await fetch(
      `http://localhost:8080/posSystem/customer?id=${id}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id: id,
          name: name,
          address: address,
          salary: salary,
        }),
      }
    );
  } catch (error) {
    console.error("updateCustomer:", error);
  }
}
