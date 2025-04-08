import { useEffect, useState } from "react";
import {
  getSellers,
  deleteSeller,
  editSeller,
  saveSellers,
} from "../../api/RetailerService";
import axios from "axios";

const Seller = () => {
  const [sellers, setSellers] = useState([]);
  const [search, setSearch] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [sellerForm, setSellerForm] = useState({
    retailerName: "",
    retailerContactNo: "",
    retailerAddress: "",
    retailerEmail: "",
  });
  const [sellerId,setSellerId]=useState("");
  const [isViewMode, setIsViewMode] = useState(false);
  const [editingSeller, setEditingSeller] = useState(null);

  useEffect(() => {
    fetchSellers();
  }, []);

  const fetchSellers = async () => {
    try {
      const response = await getSellers();
      setSellers(Array.isArray(response) ? response : response?.data || []);
    } catch (error) {
      console.error("Error fetching sellers:", error);
      setSellers([]);
    }
  };

  const handleDeleteSeller = async (sellerId) => {
    try {
      const response = deleteSeller(sellerId);
      setSellers((prevSellers) =>
        prevSellers.filter((s) => s.sellerId !== sellerId)
      );
      if (response){
        window.alert(`seller ${sellerId} successfully deleted.`)
        location.reload();
      }
    } catch (error) {
      console.error("Error deleting seller:", error);
    }
  };

  const handleInputChange = (e) => {
    setSellerForm((prevForm) => ({
      ...prevForm,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingSeller) {
        await editSeller({ ...editingSeller, ...sellerForm });
      } else {
        await saveSellers(sellerForm);
      }
      setSellerForm({
        retailerName: "",
        retailerContactNo: "",
        retailerAddress: "",
        retailerEmail: "",
      });
      setEditingSeller(null);
      setShowPopup(false);
      location.reload();
    } catch (error) {
      console.error("Error saving seller:", error);
    }
  };

  const handleEdit = (seller) => {
    setEditingSeller(seller);
    setSellerForm(seller);
    setShowPopup(true);
  };
  const handleView = (seller) => {
    setSellerForm({
      retailerName: seller.retailerName,
      retailerContactNo: seller.retailerContactNo,
      retailerAddress: seller.retailerAddress,
      retailerEmail: seller.retailerEmail,
    });
    console.log(seller?.retailerId)
    setSellerId(seller?.retailerId)

    setIsViewMode(true); // Set view mode to true
    setEditingSeller(null); // Ensure we are not in edit mode
    setShowPopup(true);
  };

  /*const filteredSellers = sellers.filter((seller) =>
    seller.sellerName.toLowerCase().includes(search.toLowerCase())
  );*/

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">All Sellers</h2>

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
            setEditingSeller(null); // Ensure we are not in edit mode
            setSellerForm({
              retailerName: "",
              retailerContactNo: "",
              retailerAddress: "",
              retailerEmail: "",
            }); // Reset form
            setShowPopup(true);
          }}
        >
          + Add new seller
        </button>
      </div>

      {/* Table */}
      <div className="bg-white p-4 shadow rounded-lg">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3">SellerCode</th>
              <th className="p-3">Name</th>
              <th className="p-3">Contact</th>
              <th className="p-3">Address</th>
              <th className="p-3">Email</th>
              <th className="p-3">Actions</th>
            </tr>
          </thead>
          <tbody>
            {sellers.map((seller) => (
              <tr
                key={seller?.retailerId}
                className="border-b hover:bg-gray-50"
              >
                <td className="p-3">{seller?.retailerId}</td>
                <td className="p-3">{seller?.retailerName}</td>
                <td className="p-3">{seller?.retailerContactNo}</td>
                <td className="p-3">{seller?.retailerAddress}</td>
                <td className="p-3">{seller?.retailerEmail}</td>
                <td className="p-3 flex space-x-4">
                  <button
                    className="text-green-500"
                    onClick={() => handleView(seller)}
                  >
                    View
                  </button>

                  <button
                    className="text-blue-600"
                    onClick={() => handleEdit(seller)}
                  >
                    Edit
                  </button>
                  {<button
                    className="text-red-600"
                    onClick={() => handleDeleteSeller(seller?.retailerId)}
                  >
                    Delete
                  </button>}
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
                setEditingSeller(null);
                setIsViewMode(false); // Reset view mode
                setSellerForm({
                  retailerName: "",
                  retailerContactNo: "",
                  retailerAddress: "",
                  retailerEmail: "",
                });
              }}
            >
              ×
            </button>

            {/* Title */}
            <h3 className="text-2xl font-semibold mb-4 text-gray-800">
              {isViewMode
                ? "View Seller"
                : editingSeller
                  ? "Edit Seller"
                  : "Add Seller"}
            </h3>

            {/* Form and Scrollable Container */}
            <div className="flex-1 overflow-y-auto max-h-[calc(100vh-6rem)] no-scrollbar">
              <form onSubmit={handleSubmit} className="space-y-4">
                {/* Input Fields */}

                {isViewMode && (
                  <div className="space-y-3">
                    <label className="block text-gray-700">Seller Code</label>
                    <input
                      type="text"
                      value={sellerId || ""}
                      readOnly
                      className="w-full border border-gray-300 p-2 rounded bg-gray-200 cursor-not-allowed"
                    />
                  </div>
                )}
                <div className="space-y-3">
                  <label className="block text-gray-700">Name*</label>
                  <input
                    type="text"
                    name="retailerName"
                    placeholder="Seller Name"
                    value={sellerForm?.retailerName || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${isViewMode
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
                    name="retailerContactNo"
                    placeholder="Seller Contact"
                    value={sellerForm?.retailerContactNo || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${isViewMode
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
                    name="retailerAddress"
                    placeholder="Seller Address"
                    value={sellerForm?.retailerAddress || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${isViewMode
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
                    name="retailerEmail"
                    placeholder="Email Address"
                    value={sellerForm?.retailerEmail || ""}
                    onChange={handleInputChange}
                    readOnly={isViewMode} // Make it read-only in view mode
                    className={`w-full border border-gray-300 p-2 rounded ${isViewMode
                        ? "bg-gray-200 cursor-not-allowed"
                        : "focus:outline-none focus:ring-2 focus:ring-blue-500"
                      }`}
                    required
                  />
                </div>

                {/* Save and Close Button */}
                {!isViewMode && (
                  <button
                    type="submit"
                    className="w-full bg-blue-600 text-white p-3 rounded-md hover:bg-blue-700 transition duration-200"
                  >
                    {editingSeller ? "Update Seller" : "Save and Close"}
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




export default Seller;