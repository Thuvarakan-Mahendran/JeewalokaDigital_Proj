import React from 'react'

export default function SellerOrder() {
  return (
    <div className="p-6 bg-gray-50 min-h-screen">
        <h2 className='text-2xl font-semibold text-gray-800'>Seller Order</h2>
        <div className="bg-white p-4 shadow rounded-lg flex justify-between mb-4 items-center">
            <input type='text' placeholder='Search...' className="w-1/3 rounded border border-gray-300 py-2 px-5"/>
        </div>
        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg">Create Order</button><br/><br/>
        <div className="bg-white p-4 shadow rounded-lg">
            <table className="w-full text-left border-collapse">
            <thead>
                <tr>
                    <th>OrderId</th>
                    <th>Date</th>
                    <th>SellerName</th>
                    <th>States</th>
                    <th>ACTIONS</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
            </table>
        </div>
    </div>
  )
}
