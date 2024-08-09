export default class OrderDetailTableDto {
  constructor( orderId, itemCode, itemName, itemPrice, quantity, total) {
    this.orderId = orderId;
    this.itemCode = itemCode;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
    this.quantity = quantity;
    this.total = total;
  }
}