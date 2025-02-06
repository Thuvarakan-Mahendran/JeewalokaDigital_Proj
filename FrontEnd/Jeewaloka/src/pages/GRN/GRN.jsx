import { useEffect, useState } from "react";
import api from "../../api/axiosInstance";

const GRN = () => {
  const [grns, setGrns] = useState([]);
  const [search, setSearch] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [grnForm, setGrnForm] = useState({
    grnId: "",
    grnSupplierId: "",
    grnReceivedBy: "",
    grnTotalAmount: 0,
    grnStatus: "",
    grnItems: [],
  });
  const [editingGrn, setEditingGrn] = useState(null);

  useEffect(() => {
    fetchGRNs();
  }, []);

  const fetchGRNs = async () => {
    try {
      const response = await api.get("/grns/getallgrns");
      setGrns(response.data?.data || []);
    } catch (error) {
      console.error("Failed to fetch GRNs:", error);
      setGrns([]);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/grns/deletegrn/${id}`);
      fetchGRNs();
    } catch (error) {
      console.error("Failed to delete GRN:", error);
    }
  };

  const handleInputChange = (e, index = null) => {
    const { name, value } = e.target;
    if (index !== null) {
      const updatedItems = [...grnForm.grnItems];
      updatedItems[index] = { ...updatedItems[index], [name]: value };
      setGrnForm({ ...grnForm, grnItems: updatedItems });
    } else {
      setGrnForm({ ...grnForm, [name]: value });
    }
  };

  const handleAddItem = () => {
    setGrnForm((prevForm) => ({
      ...prevForm,
      grnItems: [
        ...prevForm.grnItems,
        { itemId: "", quantity: 0, unitPrice: 0, totalAmount: 0 },
      ],
    }));
  };

  const handleRemoveItem = (index) => {
    const updatedItems = grnForm.grnItems.filter((_, i) => i !== index);
    setGrnForm({ ...grnForm, grnItems: updatedItems });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      editingGrn
        ? await api.put(`/grns/editgrn/${editingGrn.grnId}`, grnForm)
        : await api.post("/grns/creategrn", grnForm);
      setShowPopup(false);
      setEditingGrn(null);
      fetchGRNs();
    } catch (error) {
      console.error("Failed to save GRN:", error);
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">Goods Received Notes (GRNs)</h2>

      <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
        <input
          type="text"
          placeholder="Search..."
          className="w-1/3 rounded border border-gray-300 py-2 px-5"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg" onClick={() => setShowPopup(true)}>
          + Add New GRN
        </button>
      </div>

      <div className="bg-white p-4 shadow rounded-lg">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3">GRN ID</th>
              <th className="p-3">Supplier Name</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {grns.map((grn) => (
              <tr key={grn.grnId} className="border-b">
                <td className="p-3">{grn.grnId}</td>
                <td className="p-3">{grn.grnSupplierName}</td>
                <td className="p-3">{grn.grnStatus}</td>
                <td className="p-3 flex space-x-4">
                <button className="text-blue-600" onClick={() => { setEditingGrn(grn); setShowPopup(true); }}>View</button>
                  <button className="text-green-600" onClick={() => { setEditingGrn(grn); setShowPopup(true); }}>Edit</button>
                  <button className="text-red-600" onClick={() => handleDelete(grn.grnId)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showPopup && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-2/3 relative">
            <h3 className="text-xl font-bold mb-4">{editingGrn ? "Edit GRN" : "Add GRN"}</h3>
            <button className="absolute top-2 right-2 text-gray-600" onClick={() => setShowPopup(false)}>X</button>
            <form onSubmit={handleSubmit}>
              <input type="text" name="grnSupplierId" placeholder="Supplier ID" value={grnForm.grnSupplierId} onChange={handleInputChange} className="border p-2 rounded w-full mb-2" />
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-gray-100">
                    <th className="p-3">Item ID</th>
                    <th className="p-3">Quantity</th>
                    <th className="p-3">Unit Price</th>
                    <th className="p-3">Total</th>
                    <th className="p-3">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {grnForm.grnItems.map((item, index) => (
                    <tr key={index} className="border-b">
                      <td><input type="text" name="itemId" value={item.itemId} onChange={(e) => handleInputChange(e, index)} className="border p-2 rounded w-full" /></td>
                      <td><input type="number" name="quantity" value={item.quantity} onChange={(e) => handleInputChange(e, index)} className="border p-2 rounded w-full" /></td>
                      <td><input type="number" name="unitPrice" value={item.unitPrice} onChange={(e) => handleInputChange(e, index)} className="border p-2 rounded w-full" /></td>
                      <td>{item.quantity * item.unitPrice}</td>
                      <td><button type="button" onClick={() => handleRemoveItem(index)} className="text-red-600">🗑</button></td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <button type="button" onClick={handleAddItem} className="bg-green-600 text-white px-4 py-2 rounded-lg mt-2">+ Add Line</button>
              <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-lg mt-4">Save</button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
export default GRN;
