import OrderTableDto from "../tableDto/OrderTableDto.js";

export async function getAllOrders(){
  try {
    const response = await fetch("http://localhost:8080/posSystem/order");
    const data = await response.json();
    const orderTableList = data.map(
      (order) =>
       new OrderTableDto(
          order.orderId,
          order.orderDate,
          order.customerId,
          order.total,
          order.subTotal,
          order.paidAmount,
          order.discount,
          order.balance,
          order.address,
          order.orderDetails
       )
    );
    return orderTableList;
  } catch (error) {
    console.error("getAllOrders:", error);
  }
}

export async function saveOrder(
  orderId,
  orderDate,
  cusId,
  total,
  subTotal,
  paidAmount,
  discount,
  balance,
  address,
  orderDetails){
  try {
    console.log('received data:', orderId, orderDate, cusId, total, subTotal, paidAmount, discount, balance, address, orderDetails);
      const response = await fetch('http://localhost:8080/posSystem/order',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ 
            orderId: orderId,
            orderDate: orderDate,
            customerId: cusId,
            total: total,
            subTotal: subTotal,
            paidAmount: paidAmount,
            discount: discount,
            balance: balance,
            address: address,
            orderDetails: orderDetails
           }),
        }
      );
      let responseText = await response.text();
      return responseText === 'Success';
  } catch (error) {
    console.error('save item: ',error); 
  }
}

function isOrderExist(orderId){
  
}

export async function searchOrder(orderId){
  try {
    const response = await fetch(`http://localhost:8080/posSystem/order?orderId=${orderId}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );
    const order = await response.json();
    console.log(order);
    return new OrderTableDto(
          order.orderId,
          order.orderDate,
          order.customerId,
          order.total,
          order.subTotal,
          order.paidAmount,
          order.discount,
          order.balance,
          order.address,
          order.orderDetails
       );
  } catch (error) {
    console.error(error);
  }
}
