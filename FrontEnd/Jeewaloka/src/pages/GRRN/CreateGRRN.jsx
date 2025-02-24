import { useState, useEffect } from "react";
import Select from "react-select";
import { createGRRN } from "../../api/GRRNService";
import { useNavigate } from "react-router-dom";
import { getSuppliers } from "../../api/SupplierService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";

const CreateGRRN = () => {
  const [grrnData, setGRRNData] = useState({
    grrnSupplierId: "",
    grrnReceivedBy: "",
    grrnItems: [],
  });

  const [grrnItem, setGRRNItem] = useState({
    itemId: "",
    quantity: 0,
    unitPrice: 0,
    itemExpiryDate: "",
  });

  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);

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
    setGRRNData((prevData) => ({
      ...prevData,
      grrnSupplierId: selectedOption ? selectedOption.value : "",
    }));
  };

  const navigate = useNavigate();

  const handleInputChange = (e) => {
    setGRRNData({ ...grrnData, [e.target.name]: e.target.value });
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="text-2xl font-semibold text-gray-800">Create New GRRN</h2>

      <form className="bg-white p-4 shadow rounded-lg">
        <div className="mb-4">
          <label className="block mb-2">Supplier</label>
          <Select
            options={suppliers}
            isLoading={loading}
            isSearchable
            onChange={handleChange}
            value={suppliers.find((s) => s.value === grrnData.grrnSupplierId) || null}
            placeholder="Select a supplier..."
            className="w-full"
          />
        </div>

        <div className="mb-4">
          <label className="block mb-2">Received By</label>
          <input
            type="text"
            name="grrnReceivedBy"
            value={grrnData.grrnReceivedBy}
            onChange={handleInputChange}
            className="w-full p-2 border rounded"
          />
        </div>
      </form>
    </div>
  );
};

export default CreateGRRN;
