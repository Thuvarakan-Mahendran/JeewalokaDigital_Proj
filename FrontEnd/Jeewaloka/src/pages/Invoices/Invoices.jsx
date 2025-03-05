import React, { useEffect, useState } from 'react'
// import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
// import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
// import { DatePicker } from '@mui/x-date-pickers/DatePicker';
// import longFormatters from 'date-fns/_lib/format/longFormatters';
// import { parse } from 'date-fns';
import {
    saveBill,
    getBills
} from "../../api/InvoiceService"
import { Link, useNavigate } from 'react-router-dom'

const Invoices = () => {
    const [invoices, setInvoices] = useState([])
    const [search, setSearch] = useState("")
    const [idate, setIdate] = useState("")
    const navigate = useNavigate();

    useEffect(() => {
        fetchInvoices();
    }, [])

    const fetchInvoices = async () => {
        try {
            const response = await getBills();
            setInvoices(Array.isArray(response) ? response : response?.data || []);
            // console.log(response)
        } catch (error) {
            console.error("Error fetching suppliers:", error);
            setInvoices([]);
        }
    }

    // const handleInputChange = (e) => {
    //     const { name, value } = e.target
    //     setSearch((prev) => ({
    //         ...prev,
    //         [name]: value
    //     }))
    // }

    const handleDownloadBill = async () => {
        try { }
        catch (error) { }
    }

    const filteredInvoices = invoices.filter((invoice) => {
        // invoice.BillNO.toString().includes(search.toString())
        const matchesBillNo = search ? invoice.billNO.toString().includes(search.toString()) : true
        const matchesIssuedDate = idate ? new Date(invoice.date) === new Date(idate) : true
        // let matchesIssuedDate = true;
        // if (idate) {
        //     const invoiceDate = parse(invoice.date, "yyyy/MM/dd", new Date());
        //     const inputDate = new Date(idate);
        //     matchesIssuedDate = invoiceDate.getTime() === inputDate.getTime();
        // }
        return matchesBillNo && matchesIssuedDate
    })

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
                    value={idate}
                    onChange={(e) => setIdate(e.target.value)}
                />
                {/* <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DatePicker
                        label="Date picker"
                        value={search}
                        onChange={(e) => {
                            setIdate(e.target.value);
                        }}
                        renderInput={(params) => <TextField {...params} />}
                    />
                </LocalizationProvider> */}
            </div>
            <button
                className="bg-blue-600 text-white px-4 py-2 rounded-lg ml-6 mt-2"
                onClick={() => { navigate('/dashboard/sales/invoices/createInvoice') }}
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
                                key={invoice.billNO}
                                className="border-b hover:bg-gray-50"
                            >
                                <td className="p-3">{invoice.billNO}</td>
                                <td className="p-3">{invoice.userID}</td>
                                <td className="p-3">{invoice.retailerID}</td>
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
