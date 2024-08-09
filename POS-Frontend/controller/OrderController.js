import { getAll, getCus } from "../model/CustomerModel.js";
import { getItems, getItem } from "../model/ItemModel.js";
import { getAllOrders, searchOrder, saveOrder } from "../model/OrderModel.js";
import OrderTableDto from "../tableDto/OrderTableDto.js";
import OrderDetailTableDto from "../tableDto/OrderDetailTableDto.js";
export { clearAllFields, setDate, generateNextOrderID, setCustomerList, setItemList };

setDate();
generateNextOrderID();
setCustomerList();
setItemList();

function setDate() {
  let date = new Date();
  let formattedDate = `${
    date.getMonth() + 1
  }/${date.getDate()}/${date.getFullYear()}`;
  $("#order-date").val(formattedDate);
}

async function generateNextOrderID() {
  let id = "ORD00-001";
  let orders = await getAllOrders();
  let lastOrder = orders[orders.length - 1];
  if (lastOrder) {
    let lastID = lastOrder.orderId;
    let number = parseInt(lastID.substring(6));
    number++;
    if (number < 10) {
      id = `ORD00-00${number}`;
    } else if (number < 100) {
      id = `ORD00-0${number}`;
    } else {
      id = `ORD00-${number}`;
    }
  }
  $("#order-id").val(id);
}

async function setCustomerList() {
  let customerList = $("#customer-list");
  customerList.html("");
  let defaultOption = $("<option></option>");
  defaultOption.text("Select Customer");
  defaultOption.val("");
  customerList.append(defaultOption);
  let customers = await getAll();
  customers.forEach((customer) => {
    let option = $("<option></option>");
    option.text(customer.id);
    option.val(customer.id);
    customerList.append(option);
  });
}

$("#customer-list").change(async function () {
  let selectedId = $("#customer-list").val();
  setCusDetails(selectedId);
});

async function setCusDetails(cusId) {
  let customer = await getCus(cusId);
  $("#cus-id-order").val(customer.id);
  $("#cus-name-order").val(customer.name);
  $("#cus-address-order").val(customer.address);
  $("#cus-salary-order").val(customer.salary);
}

async function setItemList() {
  let itemList = $("#item-list");
  itemList.html("");
  let defaultOption = $("<option></option>");
  defaultOption.text("Select Item");
  defaultOption.val("");
  itemList.append(defaultOption);
  let items = await getItems();
  items.forEach((item) => {
    let option = $("<option></option>");
    option.text(item.code);
    option.val(item.code);
    itemList.append(option);
  });
}

$("#item-list").change(async function () {
  let selectedId = $("#item-list").val();
  let item = await getItem(selectedId);
  console.log(item);
  $("#item-code-order").val(item.code);
  $("#item-name-order").val(item.name);
  $("#item-qty-order").val(item.qty);
  $("#item-price-order").val(item.unitPrice);
});

$("#order-id").keydown(async function (event) {
  if (event.key === "Enter") {
    let orderId = $("#order-id").val();
    let order = await searchOrder(orderId);
    console.log("at", order);
    if (order) {
      $("#order-date").val(order.orderDate);
      $("#cus-address-order").val(order.address);
      $("#total-order").text(order.total);
      $("#sub-total-order").text(order.subTotal);
      $("#discount").val(order.discount);
      $("#paidAmount").val(order.paidAmount);
      $("#balance").val(order.balance);
      setCusDetails(order.customerId);

      let table = $("#cart-table").find("tbody");
      table.html("");
      if (Array.isArray(order.orderDetails)) {
        order.orderDetails.forEach((orderDetail) => {
          let row = `<tr>
                      <td>${orderDetail.itemCode}</td>
                      <td>${orderDetail.itemName}</td>
                      <td>${orderDetail.itemPrice}</td>
                      <td>${orderDetail.quantity}</td>
                      <td>${orderDetail.total}</td>
                      <td><button type="button" class="btn btn-danger">Remove</button></td>
                    </tr>`;
          table.append(row);
        });
      }
    } else {
      alert("Order not found!");
    }
  }
});

$("#discount").on("input", function () {
  event.preventDefault();
  let subTotal = parseFloat($("#sub-total-order").text()) || 0;
  let discount = parseFloat($("#discount").val()) || 0;
  let discountedTotal = subTotal - (subTotal * discount) / 100;
  $("#total-order").text(discountedTotal.toFixed(2));
  generateTheBalance(discountedTotal);
});

function generateTheBalance(discountedTotal) {
  let paidAmount = parseFloat($("#paidAmount").val()) || 0;
  let balance = paidAmount - discountedTotal;
  $("#balance").val(balance.toFixed(2));
}

$("#additem-btn").click(function () {
  event.preventDefault();
  let orderId = $("#order-id").val();
  let itemCode = $("#item-code-order").val();
  let itemName = $("#item-name-order").val();
  let itemPrice = parseFloat($("#item-price-order").val());
  let itemQty = parseInt($("#ordered-qty").val());
  let total = itemPrice * itemQty;

  if (
    orderId === "" ||
    itemCode === "" ||
    itemName === "" ||
    itemQty === "" ||
    itemPrice === ""
  ) {
    alert("Select an item and add order quantity!");
  } else {
    let orderDetail = new OrderDetailTableDto(
      orderId,
      itemCode,
      itemName,
      itemPrice,
      itemQty,
      total
    );
    addOrderDetailToCart(orderDetail);
    clearItemFields();
    updateTotal();
    setItemList();
  }
});

function addOrderDetailToCart(orderDetail) {
  let $tableBody = $("#cart-table tbody");

  let $existingItem = $tableBody.find("tr").filter(function () {
    return $(this).find("td").eq(0).text() === orderDetail.itemCode;
  });

  if ($existingItem.length > 0) {
    let existingQty = parseInt($existingItem.find("td").eq(3).text(), 10);
    let newQty = existingQty + parseInt(orderDetail.quantity, 10);
    $existingItem.find("td").eq(3).text(newQty);
    $existingItem
      .find("td")
      .eq(4)
      .text(orderDetail.itemPrice * newQty);
  } else {
    let $newRow = $("<tr></tr>");
    $("<td></td>").text(orderDetail.itemCode).appendTo($newRow);
    $("<td></td>").text(orderDetail.itemName).appendTo($newRow);
    $("<td></td>").text(orderDetail.itemPrice).appendTo($newRow);
    $("<td></td>").text(orderDetail.quantity).appendTo($newRow);
    $("<td></td>").text(orderDetail.total).appendTo($newRow);
    $("<td></td>")
      .html('<button type="button" class="btn btn-danger">Remove</button>')
      .appendTo($newRow);

    $tableBody.append($newRow);
  }
}

function clearItemFields() {
  $("#item-code-order").val("");
  $("#item-name-order").val("");
  $("#item-qty-order").val("");
  $("#item-price-order").val("");
  $("#ordered-qty").val("");
}

function updateTotal() {
  let $tableBody = $("#cart-table tbody");
  let total = 0;
  $tableBody.find("tr").each(function () {
    total += parseFloat($(this).find("td").eq(4).text());
  });
  $("#sub-total-order").text(total);
  $("#total-order").text(total);
}

$("#cart-table").click(function (event) {
  if (event.target && event.target.nodeName === "BUTTON") {
    let row = event.target.parentNode.parentNode;
    row.remove();
    updateTotal();
  }
});

$("#btn-purchase").click(async function () {
  event.preventDefault();

  let orderId = $("#order-id").val();
  let orderDate = $("#order-date").val();
  let cusId = $("#cus-id-order").val();
  let total = parseFloat($("#total-order").text());
  let subTotal = parseFloat($("#sub-total-order").text());
  let paidAmount = parseFloat($("#paidAmount").val());
  let discount = parseFloat($("#discount").val());
  let balance = parseFloat($("#balance").val());
  let address = $("#cus-address-order").val();

  if (
    orderId !== "" &&
    orderDate !== "" &&
    cusId !== "" &&
    total !== "" &&
    subTotal !== "" &&
    paidAmount !== "" &&
    discount !== "" &&
    balance !== "" &&
    address !== ""
  ) {
    let orderDetails = [];
    let table = $("#cart-table").find("tbody");
    table.find("tr").each(function () {
      let itemCode = $(this).find("td").eq(0).text();
      let itemName = $(this).find("td").eq(1).text();
      let itemPrice = parseFloat($(this).find("td").eq(2).text());
      let total = parseFloat($(this).find("td").eq(4).text());
      let quantity = $(this).find("td").eq(3).text();
      let orderDetail = new OrderDetailTableDto(
        orderId,
        itemCode,
        itemName,
        itemPrice,
        quantity,
        total
      );
      orderDetails.push(orderDetail);
    });

    let resp = await saveOrder(
      orderId,
      orderDate,
      cusId,
      total,
      subTotal,
      paidAmount,
      discount,
      balance,
      address,
      orderDetails
    );
    
    if (resp) {
      alert("Order has been placed successfully!");
      clearAllFields();
      setCustomerList();
      setItemList();
    }
  } else {
    alert("Fill all the fields!");
  }
});

function clearAllFields() {
  $("#order-id").val("");
  $("#order-date").val("");
  $("#cus-id-order").val("");
  $("#cus-name-order").val("");
  $("#cus-address-order").val("");
  $("#cus-salary-order").val("");
  $("#total-order").text("0.00");
  $("#sub-total-order").text("0.00");
  $("#discount").val("");
  $("#paidAmount").val("");
  $("#balance").val("");
  $("#cart-table").find("tbody").html("");
  clearItemFields();
  generateNextOrderID();
  setDate();
}
