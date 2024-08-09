import ItemTableDto from "../tableDto/ItemTableDto.js";
import {
  isExistItem,
  saveItem,
  removeItem,
  itemUpdate,
  getItems,
} from "../model/ItemModel.js";

export {getAllItems};


clearTable();
clearFields();
getAllItems();
generateNextItemCode();

//save item
$("#add-item").click(async function () {
  event.preventDefault();
  let code = $("#item-code").val();
  let name = $("#item-name").val();
  let qty = $("#item-qty").val();
  let unitPrice = $("#unit-price").val();
  try {
    if (isItemValidated(code, name, qty, unitPrice)) {
      let isItemExist = await isExistItem(code);
      isItemExist
        ? alert("Item already exist!")
        : await addItem(code, name, qty, unitPrice);
    }
  } catch (error) {
    console.error(error);
  }
});
//clear fields
$("#clear-item").click(clearFields);
//delete item
$("#remove-item").click(async function () {
  event.preventDefault();
  let code = $("#item-code").val();
  try {
    let isItemExist = await isExistItem(code);
    isItemExist ? deleteItem(code) : alert("Item not found!");
  } catch (error) {
    console.error(error);
  }
});
//update item
$("#update-item").click(async function () {
  event.preventDefault();
  let code = $("#item-code").val();
  let name = $("#item-name").val();
  let qty = $("#item-qty").val();
  let unitPrice = $("#unit-price").val();
  try {
    if (isItemValidated(code, name, qty, unitPrice)) {
      let isItemExist = await isExistItem(code);
      isItemExist
        ? await updateItem(code, name, qty, unitPrice)
        : alert("Item not found!");
    }
  } catch (error) {
    console.error(error);
  }
});
//getAllItems()
$("#getall-item").click(function () {
  event.preventDefault();
  getAllItems();
});
//set item fields when click on the table row
$("#item-table-body").click(function (event) {
  let target = event.target;
  if (target.tagName === "TD") {
    let row = target.parentNode;
    let code = row.cells[0].textContent;
    let name = row.cells[1].textContent;
    let qty = row.cells[2].textContent;
    let unitPrice = row.cells[3].textContent;

    $("#item-code").val(code);
    $("#item-name").val(name);
    $("#item-qty").val(qty);
    $("#unit-price").val(unitPrice);
  }
});
/////////////////////////////////////////////////////////////////////////////////////////////
async function addItem(id, name, qty, unitPrice) {
  try {
    if (confirm("Are you sure you want to add this item?")) {
      let response = await saveItem(id, name, qty, unitPrice);
      if (response) {
        clearFields();
        getAllItems();
        generateNextItemCode();
        alert("Item Added Successfully!");
      } else {
        alert("Failed to add item!");
      }
    }
  } catch (error) {
    console.error(error);
  }
}
async function deleteItem(code) {
  try {
    if (confirm("Are you sure you want to delete this item?")) {
      await removeItem(code);
      clearFields();
      generateNextItemCode();
      getAllItems();
      alert("Item Deleted Successfully!");
    }
  } catch (error) {
    console.error(error);
  }
}
async function updateItem(code, name, qty, unitPrice) {
  try {
    if (confirm("Are you sure you want to update this item?")) {
      await itemUpdate(code, name, qty, unitPrice);
      clearFields();
      getAllItems();
      generateNextItemCode();
      alert("Item Updated Successfully!");
    }
  } catch (error) {
    console.error(error);
  }
}
async function getAllItems() {
  try {
    let itemTableList = await getItems();
    console.log("items:", itemTableList);
    loadItemsToTable(itemTableList);
  } catch (error) {
    console.error(error);
  }
}
function loadItemsToTable(itemTableList) {
  clearTable();
  if (Array.isArray(itemTableList)) {
    itemTableList.forEach((item) => {
      reloadItemTable(item);
    });
  }
}
function reloadItemTable(item) {
  if (
    item !== null &&
    item !== undefined &&
    item instanceof ItemTableDto &&
    item.code !== null &&
    item.code !== undefined
  ) {
    let tableBody = document.getElementById("item-table-body");
    let newRow = tableBody.insertRow();

    let cellCode = newRow.insertCell(0);
    let cellName = newRow.insertCell(1);
    let cellQty = newRow.insertCell(2);
    let cellPrice = newRow.insertCell(3);

    cellCode.textContent = item.code;
    cellName.textContent = item.name;
    cellQty.textContent = item.qty;
    cellPrice.textContent = item.unitPrice;
  }
}
async function generateNextItemCode() {
  let code = "I00-001";
  let items = await getItems();
  let lastItem = items[items.length - 1];
  if (lastItem) {
    let lastCode = lastItem.code;
    let number = parseInt(lastCode.substring(4, 7));
    number++;
    if (number < 10) {
      code = `I00-00${number}`;
    } else if (number < 100) {
      code = `I00-0${number}`;
    } else {
      code = `I00-${number}`;
    }
  }
  $("#item-code").val(code);
}
function isItemValidated(code, name, qty, unitPrice) {
  let isValid = true;
  const codePattern = /^I00-\d{3}$/;
  if (!codePattern.test(code)) {
    isValid = false;
    document.getElementById('itemCode-error').textContent = 'Invalid Item Code. Format should be I00-XXX';
  } else {
    document.getElementById('itemCode-error').textContent = '';
  }
  const namePattern = /^[A-Za-z\s]+$/;
  if (name.trim() === '') {
    isValid = false;
    document.getElementById('itemName-error').textContent = 'Item Name cannot be empty';
  } else if (!namePattern.test(name)) {
    isValid = false;
    document.getElementById('itemName-error').textContent = 'Item Name cannot contain numbers or special characters';
  } else {
    document.getElementById('itemName-error').textContent = '';
  }
  if (isNaN(qty) || parseInt(qty) <= 0) {
    isValid = false;
    document.getElementById('itemQty-error').textContent = 'Quantity must be a positive number';
  } else {
    document.getElementById('itemQty-error').textContent = '';
  }
  if (isNaN(unitPrice) || parseFloat(unitPrice) <= 0) {
    isValid = false;
    document.getElementById('unitPrice-error').textContent = 'Unit Price must be a positive number';
  } else {
    document.getElementById('unitPrice-error').textContent = '';
  }
  
  return isValid;
}

function clearTable() {
  let tableBody = document.getElementById("item-table-body");
  tableBody.innerHTML = "";
}
function clearFields() {
  $("#item-code").val("");
  $("#item-name").val("");
  $("#item-qty").val("");
  $("#unit-price").val("");
  const errorTags = document.querySelectorAll("p.error");
  errorTags.forEach((tag) => (tag.textContent = ""));
  generateNextItemCode();
}
