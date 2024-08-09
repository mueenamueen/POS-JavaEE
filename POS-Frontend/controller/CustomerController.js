import CustomerTableDto from "../tableDto/CustomerTableDto.js";
import {
  getAll,
  save,
  isExist,
  deleteCus,
  update,
} from "../model/CustomerModel.js";

clearTable();
clearFields();
getAllCustomers();
generateNextCustomerID();

//save customer
$("#save-cus").click(async function () {
  event.preventDefault();
  let id = $("#cus-ID").val();
  let name = $("#cus-name").val();
  let address = $("#cus-address").val();
  let salary = $("#cus-salary").val();
  try {
    if (isValidated(id, name, address, salary)) {
      let isExistCus = await isExist(id);
      isExistCus
        ? alert("Customer already exist!")
        : saveCustomer(id, name, address, salary);
    }
  } catch (error) {
    console.error(error);
  }
});
//clear fields
$("#clear").click(function () {
  event.preventDefault();
  clearFields();
});
//delete customer
$("#remove-cus").click(async function () {
  event.preventDefault();
  let id = $("#cus-ID").val();
  try {
    let isExistCus = await isExist(id);
    isExistCus ? deleteCustomer(id) : alert("Customer not found!");
  } catch (error) {
    console.error(error);
  }
});
//update customer
$("#update-cus").click(function () {
  event.preventDefault();
  let id = $("#cus-ID").val();
  let name = $("#cus-name").val();
  let address = $("#cus-address").val();
  let salary = $("#cus-salary").val();

  if (isValidated(id, name, address, salary) && isExist(id)) {
    updateCustomer(id, name, address, salary);
  } else {
    alert("Customer not found!");
  }
});
//get all customers
$("#get-all").click(function () {
  event.preventDefault();
  getAllCustomers();
});
//////////////////////////////////////////////////////////////
async function saveCustomer(id, name, address, salary) {
  try {
    if (confirm("Are you sure you want to add this customer?")) {
      let response = await save(id, name, address, salary);
      clearFields();
      getAllCustomers();
      generateNextCustomerID();
      alert("Customer added Successfully!");
    }
  } catch (error) {
    console.error(error);
  }
}
async function deleteCustomer(id) {
  try {
    if (confirm("Are you sure you want to delete this customer?")) {
      await deleteCus(id);
      clearFields();
      getAllCustomers();
      generateNextCustomerID();
      alert("Customer Deleted Successfully!");
    }
  } catch (error) {
    console.error(error);
  }
}
async function updateCustomer(id, name, address, salary) {
  try {
    if (confirm("Are you sure you want to update this customer?")) {
      await update(id, name, address, salary);
      clearFields();
      getAllCustomers();
      generateNextCustomerID();
      alert("Customer Updated Successfully!");
    }
  } catch (error) {
    console.error(error);
  }
}
async function getAllCustomers() {
  try {
    let customerTableList = await getAll();
    console.log("Customers:", customerTableList);
    loadCustomersToTable(customerTableList);
  } catch (error) {
    console.error(error);
  }
}
// set customer details to fields
$("#cus-table-body").click(function (event) {
  let target = event.target;
  if (target.tagName === "TD") {
    let row = target.parentNode;
    let id = row.cells[0].textContent;
    let name = row.cells[1].textContent;
    let address = row.cells[2].textContent;
    let salary = row.cells[3].textContent;

    $("#cus-ID").val(id);
    $("#cus-name").val(name);
    $("#cus-address").val(address);
    $("#cus-salary").val(salary);
  }
});
function loadCustomersToTable(customerTableList) {
  clearTable();
  if (Array.isArray(customerTableList)) {
    customerTableList.forEach((customer) => {
      reloadCustomerTable(customer);
    });
  }
}
function reloadCustomerTable(customer) {
  if (
    customer !== null &&
    customer !== undefined &&
    customer instanceof CustomerTableDto &&
    customer.id !== null &&
    customer.id !== undefined
  ) {
    let tableBody = document.getElementById("cus-table-body");
    let newRow = tableBody.insertRow();

    let cellID = newRow.insertCell(0);
    let cellName = newRow.insertCell(1);
    let cellAddress = newRow.insertCell(2);
    let cellSalary = newRow.insertCell(3);

    cellID.textContent = customer.id;
    cellName.textContent = customer.name;
    cellAddress.textContent = customer.address;
    cellSalary.textContent = customer.salary;
  }
}
async function generateNextCustomerID() {
  let id = "C00-001";
  let customers = await getAll();
  let lastCustomer = customers[customers.length - 1];
  if (lastCustomer) {
    let lastID = lastCustomer.id;
    console.log(lastID);
    let number = parseInt(lastID.substring(4, 7));
    number++;
    if (number < 10) {
      id = `C00-00${number}`;
    } else if (number < 100) {
      id = `C00-0${number}`;
    } else {
      id = `C00-${number}`;
    }
  }
  $("#cus-ID").val(id);
}
function clearFields() {
  $("#cus-ID").val("");
  $("#cus-name").val("");
  $("#cus-address").val("");
  $("#cus-salary").val("");

  generateNextCustomerID();
}
function clearTable() {
  let tableBody = document.getElementById("cus-table-body");
  tableBody.innerHTML = "";
}
//validate fields
function isValidated(id, name, address, salary) {
  let isValid = true;
  const idPattern = /^C00-\d{3}$/;
  if (!idPattern.test(id)) {
    $("#cusId-error").text(
      "Invalid Customer ID format. It should be like 'C00-001'."
    );
    isValid = false;
  } else {
    $("#cusId-error").text("");
  }
  const namePattern = /^[a-zA-Z\s]+$/;
  if (name.trim() === "") {
    $("#cusName-error").text("Customer Name is required.");
    isValid = false;
  } else if (!namePattern.test(name)) {
    $("#cusName-error").text(
      "Customer Name should contain only letters and spaces."
    );
    isValid = false;
  } else {
    $("#cusName-error").text("");
  }
  const addressPattern = /^[a-zA-Z\s]+$/;
  if (address.trim() === "") {
    $("#cusAddress-error").text("Customer Address is required.");
    isValid = false;
  } else if (!addressPattern.test(address)) {
    $("#cusAddress-error").text(
      "Customer Address should contain only letters and spaces."
    );
    isValid = false;
  } else {
    $("#cusAddress-error").text("");
  }
  const salaryNumber = parseFloat(salary);
  if (isNaN(salaryNumber) || salaryNumber <= 0) {
    $("#cusSalary-error").text("Customer Salary must be a positive number.");
    isValid = false;
  } else {
    $("#cusSalary-error").text("");
  }
  return isValid;
}
