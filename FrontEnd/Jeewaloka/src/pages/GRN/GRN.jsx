import { useState } from "react";
import GRNService from "../../api/GRNService";



const GRN = () => {
  const [grnData, setGRNData] = useState({
    grnSupplierId: "",
    grnReceivedBy: "",
    grnTotalAmount: 0,
    grnStatus: "",
    grnItems: [],
  });

  const [grnItem, setGRNItem] = useState({
    grnItemId: 0,
    itemId: "",
    quantity: 0,
    unitPrice: 0,
    totalAmount: 0,
    itemExpiryDate: "",
    itemManufactureDate: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setGRNData({ ...grnData, [name]: value });
  };

  const handleItemChange = (e) => {
    const { name, value } = e.target;
    setGRNItem((prevItem) => {
      const updatedItem = { ...prevItem, [name]: value };
      if (name === "quantity" || name === "unitPrice") {
        updatedItem.totalAmount = updatedItem.quantity * updatedItem.unitPrice;
      }
      return updatedItem;
    });
  };

  const addGRNItem = () => {
    if (grnItem.itemId && grnItem.quantity > 0 && grnItem.unitPrice > 0) {
      const updatedItems = [...grnData.grnItems, grnItem];
      const updatedTotalAmount = updatedItems.reduce(
        (sum, item) => sum + item.totalAmount,
        0
      );

      setGRNData({
        ...grnData,
        grnItems: updatedItems,
        grnTotalAmount: updatedTotalAmount,
      });

      setGRNItem({
        grnItemId: 0,
        itemId: "",
        quantity: 0,
        unitPrice: 0,
        totalAmount: 0,
        itemExpiryDate: "",
        itemManufactureDate: "",
      });
    } else {
      alert("Please complete the item details before adding.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    // Ensure at least one item is added before submission
    if (grnData.grnItems.length === 0) {
      alert("Please add at least one item before submitting.");
      return;
    }
  
    try {
      await GRNService.createGRN(grnData);
      alert("GRN created successfully!");
    } catch {
      alert("Failed to create GRN.");
    }
  };
  

  return (
    <div className="max-w-3xl mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h1 className="text-2xl font-bold text-center text-gray-700 mb-6">Create GRN</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 gap-4">
          <div>
            <label className="block text-gray-600">Supplier ID:</label>
            <input
              type="text"
              name="grnSupplierId"
              value={grnData.grnSupplierId}
              onChange={handleInputChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <div>
            <label className="block text-gray-600">Received By:</label>
            <input
              type="text"
              name="grnReceivedBy"
              value={grnData.grnReceivedBy}
              onChange={handleInputChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <div>
            <label className="block text-gray-600">Total Amount:</label>
            <input
              type="number"
              name="grnTotalAmount"
              value={grnData.grnTotalAmount}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              disabled
            />
          </div>

          <div>
            <label className="block text-gray-600">Status:</label>
            <input
              type="text"
              name="grnStatus"
              value={grnData.grnStatus}
              onChange={handleInputChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>
        </div>

        <h2 className="text-xl font-semibold text-gray-700 mt-8">Add GRN Items</h2>

        <div className="grid grid-cols-1 gap-4">
          <div>
            <label className="block text-gray-600">Item ID:</label>
            <input
              type="text"
              name="itemId"
              value={grnItem.itemId}
              onChange={handleItemChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <div>
            <label className="block text-gray-600">Quantity:</label>
            <input
              type="number"
              name="quantity"
              value={grnItem.quantity}
              onChange={handleItemChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <div>
            <label className="block text-gray-600">Unit Price:</label>
            <input
              type="number"
              name="unitPrice"
              value={grnItem.unitPrice}
              onChange={handleItemChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <div>
            <label className="block text-gray-600">Total Amount:</label>
            <input
              type="number"
              name="totalAmount"
              value={grnItem.totalAmount}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              disabled
            />
          </div>

          <div>
            <label className="block text-gray-600">Expiry Date:</label>
            <input
              type="date"
              name="itemExpiryDate"
              value={grnItem.itemExpiryDate}
              onChange={handleItemChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <div>
            <label className="block text-gray-600">Manufacture Date:</label>
            <input
              type="date"
              name="itemManufactureDate"
              value={grnItem.itemManufactureDate}
              onChange={handleItemChange}
              className="mt-1 p-2 border border-gray-300 rounded-md w-full"
              required
            />
          </div>

          <button
            type="button"
            onClick={addGRNItem}
            className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
          >
            Add Item
          </button>
        </div>

        <button
          type="submit"
          className="mt-6 w-full px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600"
        >
          Create GRN
        </button>
      </form>

      <div className="mt-8">
        <h3 className="text-lg font-semibold text-gray-700">Added Items</h3>
        <ul className="mt-4 space-y-2">
          {grnData.grnItems.map((item, index) => (
            <li key={index} className="bg-gray-100 p-4 rounded-md">
              <p>Item ID: {item.itemId}</p>
              <p>Quantity: {item.quantity}</p>
              <p>Total Amount: {item.totalAmount}</p>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default GRN;
