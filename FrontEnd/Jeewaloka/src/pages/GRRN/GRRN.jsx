import React, { useState } from "react";
import api from "../../api/GRRNService";
import { useSearchParams } from "react-router-dom";

const GoodReturnNote = () => {
  const [items, setItems] = useState([]);
  const [total, setTotal] = useState(0);

  const addItem = () => {
    setItems([...items, { id: Date.now(), itemCode: "", itemName: "", pcCode: "", qty: 1 }]);
  };

  const removeItem = (id) => {
    setItems(items.filter(item => item.id !== id));
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
        {/* Search Bar */}
        <h2 className="text-2xl font-semibold text-gray-800">Goods Return Notes (GRNNs)</h2>
        <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
          <input
            type="text"
            placeholder="Search..."
            className="w-1/3 rounded border border-gray-300 py-2 px-5"
          />
        </div>
        
        {/* Add Item Button */}
        <button
            onClick={addItem}
            className="bg-blue-600 text-white px-4 py-2 rounded-md mt-4"
          >
            Add Goods Return Note
          </button>
          <br/><br/>

        {/* Table */}
        <div className="bg-white p-4 shadow rounded-lg">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr>
                <th className="p-3">GRN NO</th>
                <th className="p-3">Supplier</th>
                <th className="p-3">Received Date</th>
                <th className="p-3">Action</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.id} className="text-center">
                  <td className="border p-2">
                    <input type="text" className="w-full border p-1" placeholder="Item Code" />
                  </td>
                  <td className="border p-2">
                    <input type="text" className="w-full border p-1" placeholder="Item Name" />
                  </td>
                  <td className="border p-2">
                    <input type="text" className="w-full border p-1" placeholder="PC Code" />
                  </td>
                  <td className="border p-2">
                    <input type="number" className="w-full border p-1" defaultValue={1} />
                  </td>
                  <td className="border p-2">
                    <button onClick={() => removeItem(item.id)} className="text-red-600">Remove</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          

          
        </div>
    </div>
  );
};

export default GoodReturnNote;
