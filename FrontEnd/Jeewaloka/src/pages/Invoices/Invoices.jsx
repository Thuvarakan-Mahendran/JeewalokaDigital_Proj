import React, { useEffect, useState } from 'react'
import {
    saveBill,
    getBills
} from "../../api/InvoiceService"
import { Link, useNavigate } from 'react-router-dom'

const Invoices = () => {
    const [invoices, setInvoices] = useState([])
    const [search, setSearch] = useState("")
    const navigate = useNavigate();

    useEffect(() => {
        fetchInvoices();
    }, [])

    const fetchInvoices = async () => {
        try {
            const response = await getBills();
            setInvoices(Array.isArray(response) ? response : response?.data || []);
        } catch (error) {
            console.error("Error fetching suppliers:", error);
            setInvoices([]);
        }
    }

    const handleDownloadBill = async () => {
        try { }
        catch (error) { }
    }

    const filteredInvoices = invoices.filter((invoice) =>
        invoice.BillNO.toLowerCase().includes(search.toLowerCase())
    )

    return (
        <div>
            <h2 className="text-2xl font-semibold text-gray-800">Invoices</h2>
            <div className="bg-white p-4 shadow rounded-lg flex mb-4 items-center">
                <input
                    type="text"
                    placeholder="Search..."
                    className="w-1/3 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <input
                    type="date"
                    className="w-1/3 rounded border-[1.5px] border-stroke bg-gray-100 py-2 px-5 ml-10"
                />
            </div>
            <button
                className="bg-blue-600 text-white px-4 py-2 rounded-lg ml-6 mt-2"
                onClick={() => { navigate('/sales/invoices/createInvoice') }}
            >
                Create Invoice
            </button>
            <div className="bg-white p-4 shadow rounded-lg">
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="bg-gray-100">
                            <th className="p-3">InvoiceNO</th>
                            <th className="p-3">User</th>
                            <th className="p-3">Retailer</th>
                            <th className="p-3">Total</th>
                            <th className="p-3">Date</th>
                            <th className="p-3">Type</th>
                            <th className="p-3">Download</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredInvoices.map((invoice) => (
                            <tr
                                key={invoice.BillNO}
                                className="border-b hover:bg-gray-50"
                            >
                                <td className="p-3">{invoice.BillNO}</td>
                                <td className="p-3">{invoice.user}</td>
                                <td className="p-3">{invoice.retailer}</td>
                                <td className="p-3">{invoice.total}</td>
                                <td className="p-3">{invoice.date}</td>
                                <td className="p-3">{invoice.billCategory}</td>
                                <td className="p-3 flex space-x-4">
                                    <button
                                        className="text-green-500"
                                        onClick={() => handleDownloadBill(invoice.BillNO)}
                                    >
                                        Download
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default Invoices
