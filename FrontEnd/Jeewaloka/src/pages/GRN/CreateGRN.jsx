import { useState, useEffect } from "react";
import Select from "react-select";
import { createGRN } from "../../api/GRNService";
import { useNavigate } from "react-router-dom";
import { getSuppliers } from "../../api/SupplierService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import {getItems} from "../../api/ItemService";


const CreateGRN = () => {
  const [grnData, setGRNData] = useState({
    grnSupplierId: "",
    grnReceivedBy: "",
    grnStatus: "Pending",
    grnItems: [],
    grnTotalAmount: 0,
  });

  const [grnItem, setGRNItem] = useState({
    itemId: "",
    quantity: 0,
    unitPrice: 0,
    itemExpiryDate: "",
    itemManufactureDate: "",
    totalAmount: 0,
  });

  const [suppliers, setSuppliers] = useState([]);
  const[items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await getItems();
        if (response.statusCode === 200) {
          setItems(
            response.data.map((item) => ({
              value: item.itemId,
              label: `${item.itemName}`,
            }))
          );
        }
      } catch (error) {
        console.error("Error fetching items", error);
      } finally {
        setLoading(false);
      }
    };

    fetchItems();
  }, []);
  
  

  useEffect(() => {
    const fetchSuppliers = async () => {
      try {
        const response = await getSuppliers();
        if (response.statusCode === 200) {
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
  
  const handleChange = (selectedOption) => {
    setGRNData((prevData) => ({
      ...prevData,
      grnSupplierId: selectedOption ? selectedOption.value : "",
    }));
  };

  const navigate = useNavigate();

  // Handle changes in input fields
  const handleInputChange = (e) => {
    setGRNData({ ...grnData, [e.target.name]: e.target.value });
  };

  // Handle changes in item fields
  const handleItemChange = (e) => {
    const { name, value } = e.target;
    setGRNItem((prevItem) => {
      const updatedItem = { ...prevItem, [name]: value };

      // Ensure quantity and unitPrice are numbers before calculating total
      if (name === "quantity" || name === "unitPrice") {
        updatedItem.totalAmount =
          Number(updatedItem.quantity) * Number(updatedItem.unitPrice);
      }

      return updatedItem;
    });
  };

  // Add item to GRN
  const addGRNItem = () => {
    if (grnItem.itemId && grnItem.quantity > 0 && grnItem.unitPrice > 0) {
      const updatedItems = [...grnData.grnItems, grnItem];
      const updatedTotalAmount = updatedItems.reduce(
        (sum, item) => sum + Number(item.totalAmount),
        0
      );

      setGRNData({
        ...grnData,
        grnItems: updatedItems,
        grnTotalAmount: updatedTotalAmount,
      });

      // Reset the grnItem state after adding
      setGRNItem({
        itemId: "",
        quantity: 0,
        unitPrice: 0,
        itemExpiryDate: "",
        itemManufactureDate: "",
        totalAmount: 0,
      });
    }
  };

  
  // Submit GRN creation
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createGRN(grnData);
      navigate("/dashboard/inventary/grn"); // Redirect to the correct GRN listing page
    } catch (error) {
      console.error("Error creating GRN:", error);
    }
  };
  

  // Remove item from GRN
  const removeItem = (index) => {
    const updatedItems = [...grnData.grnItems];
    updatedItems.splice(index, 1);
    setGRNData({ ...grnData, grnItems: updatedItems });
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
        value={suppliers.find((s) => s.value === grnData.grnSupplierId) || null}
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
          <h4 className="font-semibold mb-4 text-lg text-gray-800">
            Add Item
          </h4>

          <div className="overflow-x-auto">
            <table className="min-w-full table-auto">
              <thead>
                <tr className="border-b">
                  <th className="p-2 text-left">Item ID</th>
                  <th className="p-2 text-left">Quantity</th>
                  <th className="p-2 text-left">Unit Price</th>
                  <th className="p-2 text-left">Expiry Date</th>
                  <th className="p-2 text-left">Manufacture Date</th>
                  <th className="p-2 text-left">Total Amount</th>
                  <th className="p-2 text-left">Action</th>
                </tr>
              </thead>
              <tbody>
                {grnData.grnItems.length > 0
                  ? grnData.grnItems.map((item, index) => (
                      <tr key={index} className="border-b">
                        <td className="p-2">{item.itemId}</td>
                        <td className="p-2">{item.quantity}</td>
                        <td className="p-2">Rs. {item.unitPrice}</td>
                        <td className="p-2">{item.itemExpiryDate}</td>
                        <td className="p-2">{item.itemManufactureDate}</td>
                        <td className="p-2">Rs. {item.totalAmount}</td>
                        <td className="p-2">
                          <button
                            type="button"
                            onClick={() => removeItem(index)}
                            className="px-4 py-2 text-red-500 hover:text-red-600"
                          >
                            <FontAwesomeIcon icon={faMinus}/>
                          </button>
                        </td>
                      </tr>
                    ))
                  : null}

                <tr className="border-b">
                  <td className="p-2">

                  <Select
        options={suppliers}
        isLoading={loading}
        isSearchable
        onChange={handleChange}
        value={items.find((s) => s.value === grnItem.itemId) || null}
        placeholder="Select a Item..."
        className="w-full"
      />
                
                  </td>
                  <td className="p-2">
                    <input
                      type="number"
                      name="quantity"
                      placeholder="Quantity"
                      value={grnItem.quantity}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
                    />
                  </td>
                  <td className="p-2">
                    <input
                      type="number"
                      name="unitPrice"
                      placeholder="Unit Price"
                      value={grnItem.unitPrice}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
                    />
                  </td>
                  <td className="p-2">
                    <input
                      type="date"
                      name="itemExpiryDate"
                      value={grnItem.itemExpiryDate}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
                    />
                  </td>
                  <td className="p-2">
                    <input
                      type="date"
                      name="itemManufactureDate"
                      value={grnItem.itemManufactureDate}
                      onChange={handleItemChange}
                      className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
                    />
                  </td>
                  <td className="p-2">
                    <label className="font-semibold">Rs. {grnItem.totalAmount}</label>
                  </td>
                  <td className="p-2">
                    <button
                      type="button"
                      onClick={addGRNItem}
                      className="px-4 py-2 text-green-600 hover:text-green-700 "
                    >
                     <FontAwesomeIcon icon={faPlus}/>
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
            onClick={() => navigate("/dashboard/inventary/grn")}
            className="px-4 py-2 bg-blue-600 text-white rounded"
          >
            Save GRN
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateGRN;
