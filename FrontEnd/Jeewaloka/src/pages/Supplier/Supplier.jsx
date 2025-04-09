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
  const [errors, setErrors] = useState({});
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

 const validateForm = () => {
  const newErrors = {};
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^\d{10}$/;
const faxRegex = /^\d{10}$/;
const websiteRegex = /^(https?:\/\/)?([\w-]+\.)+[a-z]{2,}(\/\S*)?$/i;

  // Required fields validation
  if (!supplierForm.supplierName.trim()) {
    newErrors.supplierName = "Supplier name is required";
  }

  if (!supplierForm.supplierContact.trim()) {
    newErrors.supplierContact = "Contact number is required";
  } else if (!phoneRegex.test(supplierForm.supplierContact.trim())) {
    newErrors.supplierContact = "Please enter a valid 10-digit phone number";
  }

  if (!supplierForm.supplierEmail.trim()) {
    newErrors.supplierEmail = "Email is required";
  } else if (!emailRegex.test(supplierForm.supplierEmail.trim())) {
    newErrors.supplierEmail = "Please enter a valid email address";
  }

  if (!supplierForm.supplierAddress.trim()) {
    newErrors.supplierAddress = "Address is required";
  }

  if (supplierForm.supplierFax.trim() && !faxRegex.test(supplierForm.supplierFax.trim())) {
    newErrors.supplierFax = "Please enter a valid 10-digit fax number";
  }

  if (supplierForm.supplierWebsite.trim() && !websiteRegex.test(supplierForm.supplierWebsite.trim())) {
    newErrors.supplierWebsite = "Please enter a valid website URL";
  }

  if (!supplierForm.supplierStatus) {
    newErrors.supplierStatus = "Status is required";
  }

  setErrors(newErrors);
  return Object.keys(newErrors).length === 0;
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
    const { name, value } = e.target;
    setSupplierForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: null,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

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
    setErrors({});
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
    setIsViewMode(true);
    setEditingSupplier(null);
    setShowPopup(true);
  };

  const filteredSuppliers = suppliers.filter((supplier) =>
    supplier.supplierName.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">All Suppliers</h2>

      {/* Search and Add Button */}
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
            setEditingSupplier(null);
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
            setErrors({});
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
                <td className="p-3">
                  <span className={`px-2 py-1 rounded-full text-xs ${
                    supplier.supplierStatus === "Active" 
                      ? "bg-green-100 text-green-800" 
                      : "bg-red-100 text-red-800"
                  }`}>
                    {supplier.supplierStatus}
                  </span>
                </td>
                <td className="p-3 flex space-x-4">
                  <button
                    className="text-green-500 hover:text-green-700"
                    onClick={() => handleView(supplier)}
                  >
                    View
                  </button>
                  <button
                    className="text-blue-600 hover:text-blue-800"
                    onClick={() => handleEdit(supplier)}
                  >
                    Edit
                  </button>
                  <button
                    className="text-red-600 hover:text-red-800"
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
            <button
              className="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
              onClick={() => {
                setShowPopup(false);
                setEditingSupplier(null);
                setIsViewMode(false);
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
                setErrors({});
              }}
            >
              ×
            </button>

            <h3 className="text-2xl font-semibold mb-4 text-gray-800">
              {isViewMode
                ? "View Supplier"
                : editingSupplier
                ? "Edit Supplier"
                : "Add Supplier"}
            </h3>

            <div className="flex-1 overflow-y-auto max-h-[calc(100vh-6rem)] no-scrollbar">
              <form onSubmit={handleSubmit} className="space-y-4">
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
                    readOnly={isViewMode}
                    className={`w-full border ${
                      errors.supplierName ? "border-red-500" : "border-gray-300"
                    } p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                  />
                  {errors.supplierName && (
                    <p className="text-red-500 text-sm">{errors.supplierName}</p>
                  )}
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Contact*</label>
                  <input
                    type="text"
                    name="supplierContact"
                    placeholder="Supplier Contact"
                    value={supplierForm?.supplierContact || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode}
                    className={`w-full border ${
                      errors.supplierContact ? "border-red-500" : "border-gray-300"
                    } p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                  />
                  {errors.supplierContact && (
                    <p className="text-red-500 text-sm">{errors.supplierContact}</p>
                  )}
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Email*</label>
                  <input
                    type="email"
                    name="supplierEmail"
                    placeholder="Email Address"
                    value={supplierForm?.supplierEmail || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode}
                    className={`w-full border ${
                      errors.supplierEmail ? "border-red-500" : "border-gray-300"
                    } p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                  />
                  {errors.supplierEmail && (
                    <p className="text-red-500 text-sm">{errors.supplierEmail}</p>
                  )}
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Address*</label>
                  <input
                    type="text"
                    name="supplierAddress"
                    placeholder="Supplier Address"
                    value={supplierForm?.supplierAddress || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode}
                    className={`w-full border ${
                      errors.supplierAddress ? "border-red-500" : "border-gray-300"
                    } p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                  />
                  {errors.supplierAddress && (
                    <p className="text-red-500 text-sm">{errors.supplierAddress}</p>
                  )}
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Fax</label>
                  <input
                    type="text"
                    name="supplierFax"
                    placeholder="Fax Number"
                    value={supplierForm?.supplierFax || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode}
                    className={`w-full border ${
                      errors.supplierFax ? "border-red-500" : "border-gray-300"
                    } p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                  />
                  {errors.supplierFax && (
                    <p className="text-red-500 text-sm">{errors.supplierFax}</p>
                  )}
                </div>

                <div className="space-y-3">
                  <label className="block text-gray-700">Website</label>
                  <input
                    type="text"
                    name="supplierWebsite"
                    placeholder="Website URL"
                    value={supplierForm?.supplierWebsite || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode}
                    className={`w-full border ${
                      errors.supplierWebsite ? "border-red-500" : "border-gray-300"
                    } p-2 rounded ${
                      isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                    }`}
                  />
                  {errors.supplierWebsite && (
                    <p className="text-red-500 text-sm">{errors.supplierWebsite}</p>
                  )}
                </div>

                <div className="space-y-2">
                  <label className="block text-gray-700">Status*</label>
                  <div className="flex items-center space-x-4">
                    <label className="flex items-center space-x-2">
                      <input
                        type="radio"
                        name="supplierStatus"
                        value="Active"
                        checked={supplierForm?.supplierStatus === "Active"}
                        onChange={handleInputChange}
                        disabled={isViewMode}
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
                        disabled={isViewMode}
                        className="text-blue-500"
                      />
                      <span className={`${isViewMode ? "text-gray-800" : ""}`}>
                        Inactive
                      </span>
                    </label>
                  </div>
                  {errors.supplierStatus && (
                    <p className="text-red-500 text-sm">{errors.supplierStatus}</p>
                  )}
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
    </div>
  );
};

export default Supplier;