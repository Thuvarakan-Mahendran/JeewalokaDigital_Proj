import {useState} from 'react'

export default function SellerReturn() {

    const [addSRN , setaddSRN] = useState(false)

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
        <h2 className='text-2xl font-semibold text-gray-800'>Seller Returns</h2>
        <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
            <input type='text' placeholder='Search...' className="w-1/3 rounded border border-gray-300 py-2 px-5"/>
        </div>
        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg" onClick={() => setaddSRN(true)}>Create Return Note</button><br/><br/>
        <div className="bg-white p-4 shadow rounded-lg">
            <table className="w-full text-left border-collapse">
            <thead>
                <tr>
                    <th>Return Id</th>
                    <th>Seller Id</th>
                    <th>SellerName</th>
                    <th>ReturnDate</th>
                    <th>ACTIONS</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
            </table>
        </div>
        {addSRN &&
            <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center">
                <div className="bg-white p-6 rounded-lg shadow-lg w-2/3 relative">
                    <h3 className="text-xl font-bold mb-4">Add Seller Reutrn Note</h3>
                    <button className="absolute top-2 right-2 text-gray-600" onClick={() => setaddSRN(false)}>X</button>
                    <input type='text' placeholder='Return No'/>
                    <input type='text' placeholder='Return Date'/>
                    <input type='text' placeholder='Seller'/>
                    <button>Add Item</button>
                    <table>
                        <thead>
                            <tr >
                                <th>Item Code</th>
                                <th>Item Name</th>
                                <th>Seller Price</th>
                                <th>Qty</th>
                                <th>Amount</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <button className="bg-blue-600 text-white px-4 py-2 rounded-lg absolute right-2">Create</button>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        }
    </div>
  )
}
