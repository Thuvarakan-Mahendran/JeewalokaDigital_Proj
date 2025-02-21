import { useEffect, useState } from "react";
import {
  getItems,
  deleteItem,
  editItem,
  saveItem,
} from "../../api/ItemService";
import { getSuppliers } from "../../api/SupplierService";
import Select from "react-select";

const Item = () => {
  const [items, setItems] = useState([]);
  const [search, setSearch] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");
  const [brandFilter, setBrandFilter] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [itemForm, setItemForm] = useState({
    itemCode: "",
    itemName: "",
    itemType: "",
    itemPurchasePrice: "",
    itemSalesPrice: "",
    supplierId: "",
    status  : "Active",
  });
  const [editingItem, setEditingItem] = useState(null);
  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchItems();
  }, []);

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

  const handleChange = (selectedOption) => {
    setItemForm((prevForm) => ({
      ...prevForm,
      supplierId: selectedOption.value,
    }));
  };

  const fetchItems = async () => {
    try {
      const response = await getItems();
      console.log("Raw API Response:", response);

      if (Array.isArray(response)) {
        setItems(response);
      } else if (response?.data && Array.isArray(response.data)) {
        setItems(response.data);
      } else {
        setItems([]);
      }
    } catch (error) {
      console.error("Error fetching items:", error);
      setItems([]);
    }
  };

  const handleDeleteItem = async (itemCode) => {
    try {
      await deleteItem(itemCode);
      setItems((prevItems) =>
        prevItems.filter((item) => item.itemCode !== itemCode)
      );
    } catch (error) {
      console.error("Error deleting item:", error);
    }
  };

  const handleInputChange = (e) => {
    setItemForm((prevForm) => ({
      ...prevForm,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingItem) {
        await editItem({ ...editingItem, ...itemForm });
      } else {
        await saveItem(itemForm);
      }
      setItemForm({
        itemCode: "",
        itemName: "",
        itemType: "",
        itemPurchasePrice: "",
        itemSalesPrice: "",
        supplierId: "",
        status: "Active",
        
      });
      setEditingItem(null);
      setShowPopup(false);
      fetchItems();
    } catch (error) {
      console.error("Error saving item:", error);
    }
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setItemForm(item);
    setShowPopup(true);
  };

  const filteredItems = items.filter(
    (item) =>
      item.itemName?.toLowerCase().includes(search.toLowerCase()) &&
      (categoryFilter ? item.itemType === categoryFilter : true) &&
      (brandFilter ? item.itemBrand === brandFilter : true)
  );

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">All Products</h2>

      {/* Search & Filters */}
      <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
        <input
          type="text"
          placeholder="Search..."
          className="w-1/3 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5 text-black outline-none transition focus:border-primary dark:border-form-strokedark dark:bg-form-input dark:text-white"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select
          className="w-1/5 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5 text-black outline-none transition focus:border-primary dark:border-form-strokedark dark:bg-form-input dark:text-white"
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
        >
          <option key="all-categories" value="">
            All Categories
          </option>
          {Array.from(new Set(items.map((item) => item.itemType))).map(
            (category, index) => (
              <option key={`category-${index}`} value={category}>
                {category}
              </option>
            )
          )}
        </select>
        <select
          className="w-1/5 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5 text-black outline-none transition focus:border-primary dark:border-form-strokedark dark:bg-form-input dark:text-white"
          onChange={(e) => setBrandFilter(e.target.value)}
        >
          <option key="all-suppliers" value="">
            All Suppliers
          </option>
          {Array.from(
            new Set(
              items.map(
                (item) =>
                  suppliers.find((s) => s.value === item.supplierId)?.label ||
                  "Unknown Supplier"
              )
            )
          ).map((supplier, index) => (
            <option key={`supplier-${index}`} value={supplier}>
              {supplier}
            </option>
          ))}
        </select>

        <button
          className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700"
          onClick={() => setShowPopup(true)}
        >
          + Add new product
        </button>
      </div>

      {/* Table */}
      <div className="bg-white p-4 shadow rounded-lg">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3">ProductId</th>
              <th className="p-3">Product</th>
              <th className="p-3">Category</th>
              <th className="p-3">PurchasePrice(Rs)</th>
              <th className="p-3">SalesPrice(Rs)</th>
              <th className="p-3">Supplier</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredItems.map((item) => (
              <tr key={item.itemCode} className="border-b hover:bg-gray-50">
                <td className="p-3">IT-0000{item.itemCode}</td>
                <td className="p-3">{item.itemName}</td>
                <td className="p-3">{item.itemType}</td>
                <td className="p-3">{item.itemPurchasePrice}</td>
                <td className="p-3">{item.itemSalesPrice}</td>
                <td className="p-3">
                  {suppliers
                    .find((s) => s.value === item.supplierId)
                    ?.label.split(" - ")[1] || "Unknown Supplier"}
                </td>
                <td className="p-3">
                  <span
                    className={`px-2 py-1 rounded text-white ${
                      item.status === "Active" ? "bg-green-500" : "bg-red-500"
                    }`}
                  >
                    {item.status || "Inactive"}
                  </span>
                </td>
                <td className="p-3 flex space-x-4">
                  <button
                    className="text-blue-600 font-semibold hover:text-blue-800"
                    onClick={() => handleEdit(item)}
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => handleDeleteItem(item.itemCode)}
                    className="text-red-600 font-semibold hover:text-red-800"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Popup Form */}
      {showPopup && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-end z-50">
          <div className="bg-white w-full max-w-lg h-full shadow-lg p-6 relative">
            {/* Close Button */}
            <button
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
              onClick={() => setShowPopup(false)}
            >
              ×
            </button>

            {/* Title */}
            <h3 className="text-2xl font-semibold mb-4 text-gray-800">
              {editingItem ? "Edit Product" : "Add Product"}
            </h3>

            {/* Form */}
            <div className="flex-1 overflow-y-auto max-h-[calc(100vh-6rem)] no-scrollbar">
              <form onSubmit={handleSubmit} className="space-y-4">
                {/* Input Fields */}
                <div className="space-y-3">
                  <label className="block text-gray-700">Item Name</label>
                  <input
                    type="text"
                    name="itemName"
                    placeholder="Item Name"
                    value={itemForm.itemName}
                    onChange={handleInputChange}
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-300">Category</label>
                  <select
                    name="itemType"
                    value={itemForm.itemType}
                    onChange={handleInputChange}
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value="" disabled>
                      Select a category
                    </option>
                    <option value="Electronics">Food</option>
                    <option value="Clothing">Medicine</option>
                    <option value="Furniture">Cosmetics</option>
                    <option value="Books">Books</option>
                  </select>
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Purchase Price</label>
                  <input
                    type="number"
                    name="itemPurchasePrice"
                    placeholder="Purchase Price"
                    value={itemForm.itemPurchasePrice}
                    onChange={handleInputChange}
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Sales Price</label>
                  <input
                    type="number"
                    name="itemSalesPrice"
                    placeholder="Sales Price"
                    value={itemForm.itemSalesPrice}
                    onChange={handleInputChange}
                    className="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Supplier ID</label>
                  <Select
                    options={suppliers}
                    isLoading={loading}
                    isSearchable
                    onChange={handleChange}
                    value={suppliers.find(
                      (supplier) => supplier.value === itemForm.supplierId
                    )}
                    placeholder="Select a supplier..."
                    className="w-full"
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Status</label>
                  <div className="flex space-x-4">
                    <label className="flex items-center space-x-2">
                      <input
                        type="radio"
                        name="status"
                        value="Active"
                        checked={itemForm.status === "Active"}
                        onChange={handleInputChange}
                        className="form-radio text-blue-600"
                      />
                      <span>Active</span>
                    </label>

                    <label className="flex items-center space-x-2">
                      <input
                        type="radio"
                        name="status"
                        value="Inactive"
                        checked={itemForm.status === "Inactive"}
                        onChange={handleInputChange}
                        className="form-radio text-blue-600"
                      />
                      <span>Inactive</span>
                    </label>
                  </div>
                </div>

                {/* Save Button */}
                <button
                  type="submit"
                  className="w-full bg-blue-600 text-white p-3 rounded-md hover:bg-blue-700 transition duration-200"
                >
                  {editingItem ? "Update Product" : "Save Product"}
                </button>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Item;
