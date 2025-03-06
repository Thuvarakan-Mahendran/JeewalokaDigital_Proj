import {useState} from 'react'

export default function GRRN() {

  const [AddGRRN , setAddGRRN] = useState(false)

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
        <h2 className='text-2xl font-semibold text-gray-800'>Goods Return Note</h2>
        <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
            <input type='text' placeholder='Search...' className="w-1/3 rounded border border-gray-300 py-2 px-5"/>
        </div>
        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg" onClick={() => setAddGRRN(true)}>Add Goods Return Note</button><br/><br/>
        <div className="bg-white p-4 shadow rounded-lg">
            <table className="w-full text-left border-collapse">
            <thead>
                <tr>
                    <th>GRN NO</th>
                    <th>Supplier</th>
                    <th>Received Date</th>
                    <th>ACTIONS</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
            </table>
        </div>
        {AddGRRN &&
          <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center">
            <div className="bg-white p-6 rounded-lg shadow-lg w-2/3 relative">
              <h3 className="text-xl font-bold mb-4">Add Goods Return Note</h3>
              <button className="absolute top-2 right-2 text-red-600" onClick={() => setAddGRRN(false)}>X</button>
              <input type='text' name='' className="w-1/3 rounded border border-gray-300 py-2 px-5" placeholder='GRN No'/><br/><br/>
              <input type='text' name='' className="w-1/3 rounded border border-gray-300 py-2 px-5" placeholder='Supplier'/><br/><br/>
              <input type='text' name='' className="w-1/3 rounded border border-gray-300 py-2 px-5" placeholder='Received Date'/><br/><br/>
              <table className="w-full text-left border-collapse">
                <thead>
                    <tr>
                        <th>Item Code</th>
                        <th>Item Name</th>
                        <th>PC_code</th>
                        <th>Qty</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                  <tr>
                    <button className="bg-green-600 text-white px-4 py-2 rounded-lg">Add Another Item</button>
                    <button className="bg-blue-600 text-white px-4 py-2 rounded-lg absolute right-2">Cretae Note</button>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        }
    </div>
  )
}
