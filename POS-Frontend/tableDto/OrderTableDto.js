export default class OrderTableDto {
  constructor(
    orderId,
    orderDate,
    customerId,
    total,
    subTotal,
    paidAmount,
    discount,
    balance,
    address,
    orderDetails
  ) {
    this.orderId = orderId;
    this.orderDate = orderDate;
    this.customerId = customerId;
    this.total = total;
    this.subTotal = subTotal;
    this.paidAmount = paidAmount;
    this.discount = discount;
    this.balance = balance;
    this.address = address;
    this.orderDetails = orderDetails;
  }
}
