import { useEffect, useState } from "react";
import { createPurchaseOrder, getAllPurchaseOrders } from "../../api/PurchaseOrderService";

const PurchaseOrder = () => {
  const [purchaseOrder, setPurchaseOrder] = useState({
    supplierId: "",
    orderDate: "",
    status: "",
    remarks: "",
    pItems: [],
  });

  const [item, setItem] = useState({
    itemId: "",
    quantity: "",
  });

  const [showPopup, setShowPopup] = useState(false);
  const [purchaseOrders, setPurchaseOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [fetchError, setFetchError] = useState(null);

  useEffect(() => {
    const fetchPurchaseOrders = async () => {
      try {
        const response = await getAllPurchaseOrders();
        setPurchaseOrders(response.data);
      } catch (err) {
        setFetchError(err.message);
        console.error("Error fetching purchase orders:", err); // Use the err variable
      } finally {
        setLoading(false);
      }
    };

    fetchPurchaseOrders();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPurchaseOrder({ ...purchaseOrder, [name]: value });
  };

  const handleItemChange = (e) => {
    const { name, value } = e.target;
    setItem({ ...item, [name]: value });
  };

  const addItem = () => {
    if (item.itemId && item.quantity) {
      setPurchaseOrder({
        ...purchaseOrder,
        pItems: [...purchaseOrder.pItems, item],
      });
      setItem({ itemId: "", quantity: "" });
      setShowPopup(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await createPurchaseOrder(purchaseOrder);
      alert("Purchase Order Created Successfully!");
      setPurchaseOrders([...purchaseOrders, response.data]);
      setPurchaseOrder({
        supplierId: "",
        orderDate: "",
        status: "",
        remarks: "",
        pItems: [],
      });
    } catch (err) {
      console.error("Error creating purchase order:", err); // Use the err variable
      alert("Failed to create purchase order. Please try again.");
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h1 className="text-2xl font-semibold text-gray-800 mb-4">Create Purchase Order</h1>
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <input
            type="text"
            name="supplierId"
            placeholder="Supplier ID"
            value={purchaseOrder.supplierId}
            onChange={handleInputChange}
            className="border p-2 rounded w-full"
            required
          />
          <input
            type="date"
            name="orderDate"
            value={purchaseOrder.orderDate}
            onChange={handleInputChange}
            className="border p-2 rounded w-full"
            required
          />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <input
            type="text"
            name="status"
            placeholder="Status"
            value={purchaseOrder.status}
            onChange={handleInputChange}
            className="border p-2 rounded w-full"
            required
          />
          <input
            type="text"
            name="remarks"
            placeholder="Remarks"
            value={purchaseOrder.remarks}
            onChange={handleInputChange}
            className="border p-2 rounded w-full"
          />
        </div>

        <div className="mt-4">
          <h3 className="text-lg font-semibold">Items</h3>
          <button
            type="button"
            className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 mt-2"
            onClick={() => setShowPopup(true)}
          >
            + Add Item
          </button>
          {purchaseOrder.pItems.length > 0 && (
            <table className="w-full mt-4 border-collapse border border-gray-300">
              <thead>
                <tr className="bg-gray-100">
                  <th className="border p-2">Item ID</th>
                  <th className="border p-2">Quantity</th>
                </tr>
              </thead>
              <tbody>
                {purchaseOrder.pItems.map((pItem, index) => (
                  <tr key={index} className="border">
                    <td className="border p-2">{pItem.itemId}</td>
                    <td className="border p-2">{pItem.quantity}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <button
          type="submit"
          className="bg-green-600 text-white px-4 py-2 rounded-lg shadow hover:bg-green-700"
        >
          Create Purchase Order
        </button>
      </form>

      {showPopup && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-1/3 relative">
            <h3 className="text-xl font-bold mb-4">Add Item</h3>
            <button
              className="absolute top-2 right-2 text-gray-600"
              onClick={() => setShowPopup(false)}
            >
              X
            </button>
            <div className="grid gap-4">
              <input
                type="text"
                name="itemId"
                placeholder="Item ID"
                value={item.itemId}
                onChange={handleItemChange}
                className="border p-2 rounded w-full"
              />
              <input
                type="number"
                name="quantity"
                placeholder="Quantity"
                value={item.quantity}
                onChange={handleItemChange}
                className="border p-2 rounded w-full"
              />
              <button
                className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700"
                onClick={addItem}
              >
                Add Item
              </button>
            </div>
          </div>
        </div>
      )}

      <h2 className="text-2xl font-semibold mt-8">All Purchase Orders</h2>
      {loading ? (
        <div>Loading...</div>
      ) : fetchError ? (
        <div className="text-red-500">{fetchError}</div>
      ) : (
        <table className="table-auto w-full border-collapse border border-gray-300 mt-4">
          <thead>
            <tr className="bg-gray-200">
              <th className="px-4 py-2 border">PO Number</th>
              <th className="px-4 py-2 border">Supplier</th>
              <th className="px-4 py-2 border">Order Date</th>
              <th className="px-4 py-2 border">Status</th>
              <th className="px-4 py-2 border">Remarks</th>
              <th className="px-4 py-2 border">Items</th>
            </tr>
          </thead>
          <tbody>
            {purchaseOrders.map((po) => (
              <tr key={po.poId}>
                <td className="px-4 py-2 border">{po.poId}</td>
                <td className="px-4 py-2 border">{po.supplierName}</td>
                <td className="px-4 py-2 border">{po.orderDate}</td>
                <td className="px-4 py-2 border">{po.status}</td>
                <td className="px-4 py-2 border">{po.remarks}</td>
                <td className="px-4 py-2 border">
                  {po.items && po.items.length > 0 ? (
                    po.items.map(item => (
                      <div key={item.id}>{item.itemName} (Qty: {item.quantity})</div>
                    ))
                  ) : (
                    <div>No items</div>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default PurchaseOrder;