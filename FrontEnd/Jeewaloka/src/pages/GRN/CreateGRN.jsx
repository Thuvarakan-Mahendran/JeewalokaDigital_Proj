import { useState, useEffect } from "react";
import Select from "react-select";
import { createGRN } from "../../api/GRNService";
import { useNavigate } from "react-router-dom";
import { getSuppliers } from "../../api/SupplierService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import { getItems } from "../../api/ItemService";

const CreateGRN = () => {
  const [grnData, setGRNData] = useState({
    grnSupplierId: "",
    grnReceivedBy: "",
    grnStatus: "Pending",
    grnItems: [],
    grnTotalAmount: 0, // Initialize total amount
  });

  const [grnItem, setGRNItem] = useState({
    itemId: "",
    quantity: 0,
    unitPrice: 0,
    itemExpiryDate: "",
    totalAmount: 0,
  });

  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);

  const [items, setItems] = useState([]);
  const [loadingItems, setLoadingItems] = useState(true);

  useEffect(() => {
    const fetchSuppliers = async () => {
      try {
        const response = await getSuppliers();
        if (response) {
          setSuppliers(
            response.data.map((supplier) => ({
              value: supplier.supplierId,
              label: `${supplier.supplierCode} - ${supplier.supplierName}`,
            }))
          );
        }
      } catch (error) {
        console.error("Error fetching suppliers", error);
      } finally {
        setLoading(false);
      }
    };

    fetchSuppliers();
  }, []);

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await getItems();
        if (response) {
          setItems(
            response.map((item) => ({
              value: item.itemCode,
              label: `${item.itemCode} - ${item.itemName}`,
            }))
          );
        }
      } catch (error) {
        console.error("Error fetching items", error);
      } finally {
        setLoadingItems(false);
      }
    };

    fetchItems();
  }, []);

  const handleChangeItem = (selectedOption) => {
    console.log("Selected Option:", selectedOption); // For debugging
    setGRNItem((prevItem) => ({
      ...prevItem, // Preserve other fields
      itemId: selectedOption ? selectedOption.value : "", // Update or reset itemId
    }));
  };

  const handleChange = (selectedOption) => {
    setGRNData((prevData) => ({
      ...prevData,
      grnSupplierId: selectedOption ? selectedOption.value : "",
    }));
  };

  const navigate = useNavigate();

  const handleInputChange = (e) => {
    setGRNData({ ...grnData, [e.target.name]: e.target.value });
  };

  const handleItemChange = (e) => {
    const { name, value } = e.target;
    setGRNItem((prevItem) => {
      const updatedItem = { ...prevItem, [name]: value };

      if (name === "quantity" || name === "unitPrice") {
        const quantity = Number(updatedItem.quantity) || 0;
        const unitPrice = Number(updatedItem.unitPrice) || 0;
        updatedItem.totalAmount = quantity * unitPrice;
      }

      return updatedItem;
    });
  };

  const addGRNItem = () => {
    if (grnItem.itemId && grnItem.quantity > 0 && grnItem.unitPrice > 0) {
      const updatedItems = [...grnData.grnItems, grnItem];
      const updatedTotalAmount = updatedItems.reduce(
        (sum, item) => sum + Number(item.totalAmount || 0),
        0
      );

      setGRNData({
        ...grnData,
        grnItems: updatedItems,
        grnTotalAmount: updatedTotalAmount,
      });

      setGRNItem({
        itemId: "",
        quantity: 0,
        unitPrice: 0,
        itemExpiryDate: "",
        totalAmount: 0,
      });
    }
  };

  const removeItem = (index) => {
    const updatedItems = [...grnData.grnItems];
    updatedItems.splice(index, 1);

    const updatedTotalAmount = updatedItems.reduce(
      (sum, item) => sum + Number(item.totalAmount || 0),
      0
    );

    setGRNData({
      ...grnData,
      grnItems: updatedItems,
      grnTotalAmount: updatedTotalAmount,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formattedGRNData = {
        ...grnData,
        grnItems: grnData.grnItems.map((item) => ({
          itemId: item.itemId,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
          itemExpiryDate: item.itemExpiryDate,
          totalAmount: item.totalAmount,
        })),
      };

      await createGRN(formattedGRNData);
      navigate("/dashboard/inventary/grn");
    } catch (error) {
      console.error("Error creating GRN:", error);
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">Create New GRN</h2>

      <form onSubmit={handleSubmit} className="bg-white p-4 shadow rounded-lg">
        <div className="mb-4">
          <label className="block mb-2">Supplier</label>
          <Select
            options={suppliers}
            isLoading={loading}
            isSearchable
            onChange={handleChange}
            value={
              suppliers.find((s) => s.value === grnData.grnSupplierId) || null
            }
            placeholder="Select a supplier..."
            className="w-full"
          />
        </div>

        <div className="mb-4">
          <label className="block mb-2">Received By</label>
          <input
            type="text"
            name="grnReceivedBy"
            value={grnData.grnReceivedBy}
            onChange={handleInputChange}
            className="w-full p-2 border rounded"
            required
          />
        </div>

        <div className="mb-4">
          <label className="block mb-2">Status</label>
          <select
            name="grnStatus"
            value={grnData.grnStatus}
            onChange={handleInputChange}
            className="w-full p-2 border rounded"
          >
            <option value="Pending">Pending</option>
            <option value="Completed">Completed</option>
            <option value="Cancelled">Cancelled</option>
          </select>
        </div>

        <div className="border p-4 rounded-lg mb-4 bg-white shadow-md">
          <h4 className="font-semibold mb-4 text-lg text-gray-800">Add Item</h4>
          <div className="overflow-x-auto">
            <table className="min-w-full table-auto">
              <thead>
                <tr className="border-b">
                  <th className="p-2 text-left">Item ID</th>
                  <th className="p-2 text-left">Quantity</th>
                  <th className="p-2 text-left">Unit Price</th>
                  <th className="p-2 text-left">Expiry Date</th>
                  <th className="p-2 text-left">Total Amount</th>
                  <th className="p-2 text-left">Action</th>
                </tr>
              </thead>
              <tbody>
                {grnData.grnItems.length > 0
                  ? grnData.grnItems.map((item, index) => (
                      <tr key={index} className="border-b">
                        <td className="p-2">
                          {items.find((i) => i.value === item.itemId)?.label ||
                            "Unknown Item"}
                        </td>

                        <td className="p-2">{item.quantity}</td>
                        <td className="p-2">Rs. {item.unitPrice}</td>
                        <td className="p-2">{item.itemExpiryDate}</td>
                        <td className="p-2">Rs. {item.totalAmount}</td>
                        <td className="p-2">
                          <button
                            type="button"
                            onClick={() => removeItem(index)}
                            className="px-4 py-2 text-red-500 hover:text-red-600"
                          >
                            <FontAwesomeIcon icon={faMinus} />
                          </button>
                        </td>
                      </tr>
                    ))
                  : null}
                <tr className="border-b">
                  <td className="p-2 w-1/3">
                    <td className="p-2 w-1/3">
                      <Select
                        options={items}
                        isLoading={loadingItems}
                        isSearchable
                        onChange={handleChangeItem}
                        value={
                          items.find((i) => i.value === grnItem.itemId) || null
                        }
                        placeholder="Select an item..."
                        className="w-full"
                        menuPortalTarget={document.body} // Fixes dropdown rendering issues
                        menuPosition="fixed"
                      />
                    </td>
                  </td>
                  <td className="p-2 w-1/12">
                    <input
                      type="number"
                      name="quantity"
                      placeholder="Quantity"
                      value={grnItem.quantity}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded"
                    />
                  </td>
                  <td className="p-2 w-1/5">
                    <input
                      type="number"
                      name="unitPrice"
                      placeholder="Unit Price"
                      value={grnItem.unitPrice}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded"
                    />
                  </td>
                  <td className="p-2">
                    <input
                      type="date"
                      name="itemExpiryDate"
                      value={grnItem.itemExpiryDate}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded"
                    />
                  </td>
                  <td className="p-2">
                    <label className="font-semibold">
                      Rs. {grnItem.totalAmount}
                    </label>
                  </td>
                  <td className="p-2">
                    <button
                      type="button"
                      onClick={addGRNItem}
                      className="px-4 py-2 text-green-600 hover:text-green-700"
                    >
                      <FontAwesomeIcon icon={faPlus} />
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div className="flex justify-end space-x-4">
          <button
            type="button"
            onClick={() => navigate("/dashboard/inventary/grn")}
            className="px-4 py-2 bg-gray-300 rounded"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white rounded"
          >
            Save GRN
          </button>
        </div>

        <div className="mt-6 bg-blue-50 p-4 rounded-lg shadow-md text-right">
          <h3 className="text-xl font-semibold text-gray-800">
            Total Amount:{" "}
            <span className="text-2xl font-bold text-blue-600">
              Rs. {grnData.grnTotalAmount || 0}
            </span>
          </h3>
        </div>
      </form>
    </div>
  );
};

export default CreateGRN;
