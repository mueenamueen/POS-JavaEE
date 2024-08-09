import { getAll } from "../model/CustomerModel.js";
import { getItems } from "../model/ItemModel.js";
import { getAllOrders } from "../model/OrderModel.js";
import { getAllItems } from "./ItemController.js";
import  { clearAllFields, setDate, generateNextOrderID, setCustomerList, setItemList } from "./OrderController.js";

// import ItemCart from "../cart/ItemCart";
// import { getAll, save, isExist, deleteItem, update } from "../model/ItemModel";



// import {setCustomerList, setItemList , generateNextOrderId ,clearFields,setDate} from './OrderController.js';
// import {loadAllItems} from './ItemController.js';


$(document).ready(function() {

  setDataToHomePage();

  $('#homePage').show();

  $('.nav-link').click(function(event) {
    event.preventDefault();

    $('section').hide();

    var targetSection = $(this).attr('href');

    $(targetSection).show();
    switch (targetSection) {

      case '#HomePage':
        $('.sec-name').text('POS System');
        document.title = "POS System";
        setDataToHomePage();
        break;

      case '#CustomerManage':
        $('.sec-name').text('Customer Manage');
        document.title = "Customer Manage";
        break;
      case '#ItemManage':
        $('.sec-name').text('Item Manage');
        document.title = "Item Manage";
        getAllItems();
        break;
      case '#OrderManage':
        $('.sec-name').text('Order Manage');
        document.title = "Place Order";
        clearAllFields();
        setCustomerList();
        setItemList();
        generateNextOrderID();
        setDate();
        break;
      default:
        $('.sec-name').text('POS System');
        document.title = "POS System";
    }
  });
});

async function setDataToHomePage() {
  let customers = await getAll();
  let items = await getItems();
  let orders = await getAllOrders();

  $('#total-customers').text(customers.length);
  $('#total-items').text(items.length);
  $('#total-orders').text(orders.length);
}