import { useEffect, useState } from "react";
import {
  getAllGRNs,
  deleteGRN,
  createGRN,
  updateGRN,
} from "../../api/GRNService";
import { useNavigate } from "react-router-dom";

const GRN = () => {
  const [grns, setGRNs] = useState([]);
  const [search, setSearch] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [isViewMode, setIsViewMode] = useState(false);
  const [editingGRN, setEditingGRN] = useState(null);

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

  const navigate = useNavigate();

  useEffect(() => {
    fetchGRNs();
  }, []);

  const fetchGRNs = async () => {
    try {
      const response = await getAllGRNs();
      setGRNs(Array.isArray(response) ? response : response?.data || []);
    } catch (error) {
      console.error("Error fetching GRNs:", error);
      setGRNs([]);
    }
  };

  const handleInputChange = (e) => {
    setGRNData({ ...grnData, [e.target.name]: e.target.value });
  };

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingGRN) {
        await updateGRN(editingGRN.grnId, grnData);
      } else {
        await createGRN(grnData);
      }
      setShowPopup(false);
      setEditingGRN(null);
      fetchGRNs();
    } catch (error) {
      console.error("Error saving GRN:", error);
    }
  };

  const handleDeleteGRN = async (grnId) => {
    try {
      await deleteGRN(grnId);
      setGRNs((prev) => prev.filter((g) => g.grnId !== grnId));
    } catch (error) {
      console.error("Error deleting GRN:", error);
    }
  };

  const handleEdit = (grn) => {
    setEditingGRN(grn);
    setGRNData(grn);
    setIsViewMode(false);
    setShowPopup(true);
  };

  const handleView = (grn) => {
    setGRNData(grn);
    setIsViewMode(true);
    setShowPopup(true);
  };

  const removeItem = (index) => {
    const updatedItems = [...grnData.grnItems];
    updatedItems.splice(index, 1);
    setGRNData({ ...grnData, grnItems: updatedItems });
  };

  const handleAddNewGRN = () => {
    navigate("creategrn");
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">All GRNs</h2>

      <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
        <input
          type="text"
          placeholder="Search..."
          className="w-1/3 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
         <button
          className="bg-blue-600 text-white px-4 py-2 rounded-lg"
          onClick={handleAddNewGRN} // Navigate to New GRN page
        >
          + Add new GRN
        </button>
      </div>

      <div className="bg-white p-4 shadow rounded-lg">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3">Supplier ID</th>
              <th className="p-3">Received By</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {grns.map((grn) => (
              <tr key={grn.grnId} className="border-b hover:bg-gray-50">
                <td className="p-3">{grn.grnSupplierId}</td>
                <td className="p-3">{grn.grnReceivedBy}</td>
                <td className="p-3">{grn.grnStatus}</td>
                <td className="p-3 flex space-x-4">
                  <button
                    className="text-green-500"
                    onClick={() => handleView(grn)}
                  >
                    View
                  </button>
                  <button
                    className="text-blue-600"
                    onClick={() => handleEdit(grn)}
                  >
                    Edit
                  </button>
                  <button
                    className="text-red-600"
                    onClick={() => handleDeleteGRN(grn.grnId)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showPopup && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-end z-50">
          <div className="bg-white w-full max-w-[900px] h-full shadow-lg p-6">

            <button
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
              onClick={() => {
                setShowPopup(false);
                setEditingGRN(null);
                setIsViewMode(false); // Reset view mode
                setGRNData({
                  supplierCode: "",
                  supplierName: "",
                  supplierContact: "",
                  supplierEmail: "",
                  supplierAddress: "",
                  supplierFax: "",
                  supplierWebsite: "",
                  supplierStatus: "",
                  supplierCreatedDate: "",
                });
              }}
            >
              ×
            </button>
            <h3 className="text-xl font-semibold mb-4">
              {isViewMode
                ? "View GRN"
                : editingGRN
                ? "Edit GRN"
                : "Add New GRN"}
            </h3>
            <form
              onSubmit={handleSubmit}
              className="max-h-[70vh] overflow-y-auto no-scrollbar"
            >
              {/* Supplier ID */}
              <label className="block mb-2">Supplier ID</label>
              <input
                type="number"
                name="grnSupplierId"
                value={grnData.grnSupplierId}
                onChange={handleInputChange}
                disabled={isViewMode}
                className="w-full p-2 border rounded mb-4"
              />
              <input
                type="text"
                name="grnrecivedBy"
                value={grnData.grnReceivedBy}
                onChange={handleInputChange}
                disabled={isViewMode}
                className="w-full p-2 border rounded mb-4"
              />

              <input
                type="text"
                name="grnStatus"
                value={grnData.grnStatus}
                onChange={handleInputChange}
                disabled={isViewMode}
                className="w-full p-2 border rounded mb-4"
              />

              {/* Item Entry Section */}
              {!isViewMode && (
                <div className="border p-4 rounded-lg mb-4 bg-white shadow-md">
                  <h4 className="font-semibold mb-4 text-lg text-gray-800">
                    Add Item
                  </h4>

                  {/* Display Added Items in Table */}
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
                        {/* Displaying Added Items */}
                        {/* Displaying Added Items */}
                        {grnData.grnItems.length > 0
                          ? grnData.grnItems.map((item, index) => (
                              <tr key={index} className="border-b">
                                <td className="p-2">{item.itemId}</td>
                                <td className="p-2">{item.quantity}</td>
                                <td className="p-2">Rs. {item.unitPrice}</td>
                                <td className="p-2">{item.itemExpiryDate}</td>
                                <td className="p-2">
                                  {item.itemManufactureDate}
                                </td>
                                <td className="p-2">Rs. {item.totalAmount}</td>
                                <td className="p-2">
                                  <button
                                    type="button"
                                    onClick={() => removeItem(index)}
                                    className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded shadow-md"
                                  >
                                    Remove
                                  </button>
                                </td>
                              </tr>
                            ))
                          : null}

                        {/* Empty Row for Adding New Item */}
                        {grnItem && (
                          <tr className="border-b">
                            <td className="p-2">
                              <input
                                type="text"
                                name="itemId"
                                placeholder="Item ID"
                                value={grnItem.itemId}
                                onChange={handleItemChange}
                                className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-300"
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
                              <label className="font-semibold">
                                Rs. {grnItem.totalAmount}
                              </label>
                            </td>
                            <td className="p-2">
                              <button
                                type="button"
                                onClick={() => removeItem()}
                                className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded shadow-md"
                              >
                                Remove
                              </button>
                            </td>
                          </tr>
                        )}
                      </tbody>
                    </table>
                    <button
                      type="button"
                      onClick={addGRNItem}
                      className="px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded shadow-md"
                    >
                      + Add Item
                    </button>
                  </div>
                </div>
              )}

              <div className="flex justify-end space-x-4">
                <button
                  type="button"
                  onClick={() => setShowPopup(false)}
                  className="px-4 py-2 bg-gray-300 rounded"
                >
                  Close
                </button>
                {!isViewMode && (
                  <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded"
                  >
                    Save
                  </button>
                )}
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default GRN;
