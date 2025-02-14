import { useEffect, useState } from "react";
import {
  getSuppliers,
  deleteSupplier,
  editSupplier,
  saveSupplier,
} from "../../api/SupplierService";

const Supplier = () => {
  const [suppliers, setSuppliers] = useState([]);
  const [search, setSearch] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [supplierForm, setSupplierForm] = useState({
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
  const [isViewMode, setIsViewMode] = useState(false);
  const [editingSupplier, setEditingSupplier] = useState(null);

  useEffect(() => {
    fetchSuppliers();
  }, []);

  const fetchSuppliers = async () => {
    try {
      const response = await getSuppliers();
      setSuppliers(Array.isArray(response) ? response : response?.data || []);
    } catch (error) {
      console.error("Error fetching suppliers:", error);
      setSuppliers([]);
    }
  };

  const handleDeleteSupplier = async (supplierId) => {
    try {
      await deleteSupplier(supplierId);
      setSuppliers((prevSuppliers) =>
        prevSuppliers.filter((s) => s.supplierId !== supplierId)
      );
    } catch (error) {
      console.error("Error deleting supplier:", error);
    }
  };

  const handleInputChange = (e) => {
    setSupplierForm((prevForm) => ({
      ...prevForm,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingSupplier) {
        await editSupplier({ ...editingSupplier, ...supplierForm });
      } else {
        await saveSupplier(supplierForm);
      }
      setSupplierForm({
        supplierName: "",
        supplierContact: "",
        supplierEmail: "",
        supplierAddress: "",
        supplierFax: "",
        supplierWebsite: "",
        supplierStatus: "",
      });
      setEditingSupplier(null);
      setShowPopup(false);
      fetchSuppliers();
    } catch (error) {
      console.error("Error saving supplier:", error);
    }
  };

  const handleEdit = (supplier) => {
    setEditingSupplier(supplier);
    setSupplierForm(supplier);
    setShowPopup(true);
  };
  const handleView = (supplier) => {
    setSupplierForm({
      supplierCode: supplier.supplierCode,
      supplierName: supplier.supplierName,
      supplierContact: supplier.supplierContact,
      supplierEmail: supplier.supplierEmail,
      supplierAddress: supplier.supplierAddress,
      supplierFax: supplier.supplierFax,
      supplierWebsite: supplier.supplierWebsite,
      supplierStatus: supplier.supplierStatus,
      supplierCreatedDate: supplier.supplierCreatedDate,
    });

    setIsViewMode(true); // Set view mode to true
    setEditingSupplier(null); // Ensure we are not in edit mode
    setShowPopup(true);
  };

  const filteredSuppliers = suppliers.filter((supplier) =>
    supplier.supplierName.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">All Suppliers</h2>

      {/* Search */}
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
          onClick={() => {
            setEditingSupplier(null); // Ensure we are not in edit mode
            setSupplierForm({
              supplierCode: "",
              supplierName: "",
              supplierContact: "",
              supplierEmail: "",
              supplierAddress: "",
              supplierFax: "",
              supplierWebsite: "",
              supplierStatus: "",
              supplierCreatedDate: "",
            }); // Reset form
            setShowPopup(true);
          }}
        >
          + Add new supplier
        </button>
      </div>

      {/* Table */}
      <div className="bg-white p-4 shadow rounded-lg">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3">SupCode</th>
              <th className="p-3">Name</th>
              <th className="p-3">Contact</th>
              <th className="p-3">Email</th>
              <th className="p-3">Address</th>
              <th className="p-3">Status</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredSuppliers.map((supplier) => (
              <tr
                key={supplier.supplierId}
                className="border-b hover:bg-gray-50"
              >
                <td className="p-3">{supplier.supplierCode}</td>
                <td className="p-3">{supplier.supplierName}</td>
                <td className="p-3">{supplier.supplierContact}</td>
                <td className="p-3">{supplier.supplierEmail}</td>
                <td className="p-3">{supplier.supplierAddress}</td>
                <td className="p-3">{supplier.supplierStatus}</td>
                <td className="p-3 flex space-x-4">
                  <button
                    className="text-green-500"
                    onClick={() => handleView(supplier)}
                  >
                    View
                  </button>

                  <button
                    className="text-blue-600"
                    onClick={() => handleEdit(supplier)}
                  >
                    Edit
                  </button>
                  <button
                    className="text-red-600"
                    onClick={() => handleDeleteSupplier(supplier.supplierId)}
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
          <div className="bg-white w-full max-w-lg h-full shadow-lg p-6">
            {/* Close Button */}
            <button
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
              onClick={() => {
                setShowPopup(false);
                setEditingSupplier(null);
                setIsViewMode(false); // Reset view mode
                setSupplierForm({
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

            {/* Title */}
            <h3 className="text-2xl font-semibold mb-4 text-gray-800">
              {isViewMode
                ? "View Supplier"
                : editingSupplier
                ? "Edit Supplier"
                : "Add Supplier"}
            </h3>

            {/* Form and Scrollable Container */}
            <div className="flex-1 overflow-y-auto max-h-[calc(100vh-6rem)] no-scrollbar">
              <form onSubmit={handleSubmit} className="space-y-4">
                {/* Input Fields */}

                {isViewMode && (
                  <div className="space-y-3">
                    <label className="block text-gray-700">Supplier Code</label>
                    <input
                      type="text"
                      value={supplierForm.supplierCode || ""}
                      readOnly
                      className="w-full border border-gray-300 p-2 rounded bg-gray-200 cursor-not-allowed"
                    />
                  </div>
                )}
                <div className="space-y-3">
                  <label className="block text-gray-700">Name*</label>
                  <input
                    type="text"
                    name="supplierName"
                    placeholder="Supplier Name"
                    value={supplierForm?.supplierName || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Contact</label>
                  <input
                    type="text"
                    name="supplierContact"
                    placeholder="Supplier Contact"
                    value={supplierForm?.supplierContact || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Email</label>
                  <input
                    type="email"
                    name="supplierEmail"
                    placeholder="Email Address"
                    value={supplierForm?.supplierEmail || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Address</label>
                  <input
                    type="text"
                    name="supplierAddress"
                    placeholder="Supplier Address"
                    value={supplierForm?.supplierAddress || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Fax</label>
                  <input
                    type="text"
                    name="supplierFax"
                    placeholder="Fax Number"
                    value={supplierForm?.supplierFax || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                    required
                  />
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Website</label>
                  <input
                    type="text"
                    name="supplierWebsite"
                    placeholder="Website"
                    value={supplierForm?.supplierWebsite || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                    required
                  />
                </div>

                {/* Status Radio Buttons */}
                <div className="space-y-2">
                  <label className="block text-gray-700">Status</label>
                  <div className="flex items-center space-x-4">
                    <label className="flex items-center space-x-2">
                      <input
                        type="radio"
                        name="supplierStatus"
                        value="Active"
                        checked={supplierForm?.supplierStatus === "Active"}
                        onChange={handleInputChange}
                        disabled={isViewMode} // Disable in View Mode
                        className="text-blue-500"
                      />
                      <span className={`${isViewMode ? "text-gray-800" : ""}`}>
                        Active
                      </span>
                    </label>
                    <label className="flex items-center space-x-2">
                      <input
                        type="radio"
                        name="supplierStatus"
                        value="Inactive"
                        checked={supplierForm?.supplierStatus === "Inactive"}
                        onChange={handleInputChange}
                        disabled={isViewMode} // Disable in View Mode
                        className="text-blue-500"
                      />
                      <span className={`${isViewMode ? "text-gray-800" : ""}`}>
                        Inactive
                      </span>
                    </label>
                  </div>
                </div>
                {isViewMode && (
                  <div className="space-y-3">
                    <label className="block text-gray-700">Created Date</label>
                    <input
                      type="text"
                      value={supplierForm.supplierCreatedDate || ""}
                      readOnly
                      className="w-full border border-gray-300 p-2 rounded bg-gray-200 cursor-not-allowed"
                    />
                  </div>
                )}

                {/* Save and Close Button */}
                {!isViewMode && (
                  <button
                    type="submit"
                    className="w-full bg-blue-600 text-white p-3 rounded-md hover:bg-blue-700 transition duration-200"
                  >
                    {editingSupplier ? "Update Supplier" : "Save and Close"}
                  </button>
                )}
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Add the following CSS globally or inside your component's styles */}
    </div>
  );
};

export default Supplier;
