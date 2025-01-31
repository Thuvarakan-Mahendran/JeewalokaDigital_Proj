import { useEffect, useState } from "react";
import {
  getItems,
  deleteItem,
  editItem,
  saveItem,
} from "../../api/ItemService";

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
  });
  const [editingItem, setEditingItem] = useState(null);

  useEffect(() => {
    fetchItems();
  }, []);

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
          <option key="all-brands" value="">
            All Brands
          </option>
          {Array.from(new Set(items.map((item) => item.itemBrand))).map(
            (brand, index) => (
              <option key={`brand-${index}`} value={brand}>
                {brand}
              </option>
            )
          )}
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
              <th className="p-3">Brand</th>
              <th className="p-3">Price</th>
              <th className="p-3">Stock</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredItems.map((item) => (
              <tr key={item.itemCode} className="border-b hover:bg-gray-50">
                <td className="p-3">{item.itemCode}</td>
                <td className="p-3">{item.itemName}</td>
                <td className="p-3">{item.itemType}</td>
                <td className="p-3">{item.itemBrand || "-"}</td>
                <td className="p-3">${item.itemSalesPrice}</td>
                <td className="p-3">{item.itemQuantity}</td>
                
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
        <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-1/3 relative">
            <h3 className="text-xl font-bold mb-4">
              {editingItem ? "Edit Product" : "Add Product"}
            </h3>
            <button
              className="absolute top-2 right-2 text-gray-600"
              onClick={() => setShowPopup(false)}
            >
              X
            </button>
            <form onSubmit={handleSubmit} className="grid gap-4">
              <input
                type="text"
                name="itemName"
                placeholder="Item Name"
                value={itemForm.itemName}
                onChange={handleInputChange}
              />
              <input
                type="text"
                name="itemType"
                placeholder="Category"
                value={itemForm.itemType}
                onChange={handleInputChange}
              />
              <input
                type="number"
                name="itemPurchasePrice"
                placeholder="Purchase Price"
                value={itemForm.itemPurchasePrice}
                onChange={handleInputChange}
              />
              <input
                type="number"
                name="itemSalesPrice"
                placeholder="Sales Price"
                value={itemForm.itemSalesPrice}
                onChange={handleInputChange}
              />
              <input
                type="number"
                name="supplierId"
                placeholder="SupplierId"
                value={itemForm.supplierId}
                onChange={handleInputChange}
              />
              <button type="submit">
                {editingItem ? "Update Product" : "Save Product"}
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Item;
