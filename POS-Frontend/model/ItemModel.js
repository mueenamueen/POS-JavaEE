import ItemTableDto from "../tableDto/ItemTableDto.js";

export async function getItems() {
  try {
    const response = await fetch("http://localhost:8080/posSystem/item");
    const data = await response.json();
    const itemTableList = data.map(
      (item) =>
        new ItemTableDto(
          item.code,
          item.name,
          item.qty,
          item.unitPrice
        )
    );
    return itemTableList;
  } catch (error) {
    console.error("getAllItems:", error);
  }
}

export async function getItem(code) {
  try {
    const response = await fetch(`http://localhost:8080/posSystem/item?code=${code}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );
    return response.json();
  } catch (error) {
    console.error(error);
  }
}

export async function saveItem(code, name, qty, unitPrice){
  try {
    if (code && name && qty && unitPrice) {
      const response = await fetch('http://localhost:8080/posSystem/item',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ 
            code: code,
            name: name,
            qty: qty,
            unitPrice: unitPrice
           }),
        }
      );
      let responseText = await response.text();
      return responseText === 'Item saved successfully!';
    }
  } catch (error) {
    console.error('save item: ',error); 
  }
}
export async function isExistItem(code){
  try {
    const response = await fetch(`http://localhost:8080/posSystem/item?code=${code}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );
    return response.status === 200;
  } catch (error) {
    console.error(error);
  }
}

export async function removeItem(code) {
  try {
    const response = fetch(
      `http://localhost:8080/posSystem/item?code=${code}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
  } catch (error) {
    console.error("removeItem:", error);
  }
}

export async function itemUpdate(code, name, qty, unitPrice) {
  try {
    const response = await fetch(
      `http://localhost:8080/posSystem/item?code=${code}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          code: code,
          name: name,
          qty: qty,
          unitPrice: unitPrice
        }),
      }
    );
  } catch (error) {
    console.error("updateItem:", error);
  }
}

