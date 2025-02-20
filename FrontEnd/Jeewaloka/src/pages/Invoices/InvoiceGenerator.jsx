import React, { useState } from 'react'
import { useRef } from 'react'
import jsPDF from 'jspdf'
import html2canvas from 'html2canvas'


const Input = ({ className = '', ...props }) => (
    <input
        className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
        {...props}
    />
)

const Button = ({ children, className = '', ...props }) => (
    <button
        className={`mr-2 p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
        {...props}
    >
        {children}
    </button>
)

const Select = ({ children, className = '', ...props }) => (
    <select
        className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
        {...props}
    >
        {children}
    </select>
)

const InvoiceGenerator = () => {
    const contentRef = useRef(null)
    const [invoiceData, setInvoiceData] = useState({
        invoiceNo: '',
        issueDate: '',
        retailer: '',
        paymentType: '',
        items: [],
        subtotal: 0,
        discount: 0,
        total: 0
    })

    const [currentItem, setCurrentItem] = useState({
        name: '',
        qty: '',
        unitPrice: '',
        amount: 0
    })

    const handleDownloadPDF = async () => {
        if (contentRef.current == null) {
            console.log("reference is null")
            return;
        }
        const element = contentRef.current
        const canvas = await html2canvas(element)
        const imgData = canvas.toDataURL('image/png')

        const pdf = new jsPDF('p', 'mm', 'a4'); // Portrait, millimeters, A4 size
        const imgWidth = 210; // A4 width in mm
        const imgHeight = (canvas.height * imgWidth) / canvas.width

        pdf.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight)
        pdf.save(`Invoice-${invoiceData.invoiceNo}.pdf`)
    }

    const handleInputChange = (e) => {
        const { name, value } = e.target
        setInvoiceData(prev => ({
            ...prev,
            [name]: value
        }))
    }

    const handleItemChange = (e) => {
        const { name, value } = e.target
        setCurrentItem(prev => ({
            ...prev,
            [name]: value,
            amount: name === 'qty' || name === 'unitPrice'
                ? (name === 'qty' ? value : prev.qty) * (name === 'unitPrice' ? value : prev.unitPrice)
                : prev.amount
        }))
    }

    const addItem = () => {
        if (currentItem.name && currentItem.qty && currentItem.unitPrice) {
            setInvoiceData(prev => ({
                ...prev,
                items: [...prev.items, currentItem],
                subtotal: prev.subtotal + currentItem.amount,
                total: prev.subtotal + currentItem.amount - prev.discount
            }))
            setCurrentItem({
                name: '',
                qty: '',
                unitPrice: '',
                amount: 0
            })
        }
    }

    const removeItem = (index) => {
        setInvoiceData(prev => {
            const newItems = [...prev.items]
            const removedAmount = newItems[index].amount
            newItems.splice(index, 1)
            return {
                ...prev,
                items: newItems,
                subtotal: prev.subtotal - removedAmount,
                total: prev.subtotal - removedAmount - prev.discount
            }
        })
    }

    return (
        <div className="flex gap-6 p-6 min-h-screen bg-gray-50">
            {/* Left Side - Form */}
            <div className="w-1/2 p-6 bg-white rounded-lg shadow-md">
                <h2 className="text-2xl font-bold mb-6">Create Invoice</h2>

                <div className="space-y-4">
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm mb-1">Invoice No</label>
                            <Input
                                name="invoiceNo"
                                value={invoiceData.invoiceNo}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div>
                            <label className="block text-sm mb-1">Issue Date</label>
                            <Input
                                type="date"
                                name="issueDate"
                                value={invoiceData.issueDate}
                                onChange={handleInputChange}
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm mb-1">Retailer</label>
                        <Input
                            name="retailer"
                            value={invoiceData.retailer}
                            onChange={handleInputChange}
                        />
                    </div>

                    <div>
                        <label className="block text-sm mb-1">Payment Type</label>
                        <Select
                            name="paymentType"
                            value={invoiceData.paymentType}
                            onChange={handleInputChange}
                        >
                            <option value="">Select payment terms</option>
                            <option value="CASH">CASH</option>
                            <option value="CREDIT">CREDIT</option>
                        </Select>
                    </div>
                    <div className="border p-4 rounded-lg">
                        <h3 className="text-lg font-semibold mb-4">Add Item</h3>
                        <div className="grid grid-cols-4 gap-2">
                            <Input
                                placeholder="Item name"
                                name="name"
                                value={currentItem.name}
                                onChange={handleItemChange}
                            />
                            <Input
                                type="number"
                                placeholder="Qty"
                                name="qty"
                                value={currentItem.qty}
                                onChange={handleItemChange}
                            />
                            <Input
                                type="number"
                                placeholder="Unit price"
                                name="unitPrice"
                                value={currentItem.unitPrice}
                                onChange={handleItemChange}
                            />
                            <Button onClick={addItem}>Add Item</Button>
                        </div>
                        <div>
                            <Button
                                onClick={handleDownloadPDF}
                                className='bg-[#088612] text-white mt-3'
                            >
                                Download
                            </Button>
                            <Button
                                className='mr-2'
                            >
                                Save
                            </Button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Right Side - Preview */}
            <div className="w-1/2 p-6 bg-white rounded-lg shadow-md" ref={contentRef}>
                <div className="border-b pb-4 mb-4">
                    <div className="flex justify-between items-start">
                        <div>
                            <h1 className="text-2xl font-bold">INVOICE</h1>
                            <p className="text-gray-600">Invoice No: {invoiceData.invoiceNo}</p>
                            <p className="text-gray-600">Date: {invoiceData.issueDate}</p>
                        </div>
                        <div className="text-right">
                            <h2 className="font-bold">Jeewaloka</h2>
                            <p className="text-gray-600">Address Line 1</p>
                            <p className="text-gray-600">City, State, ZIP</p>
                        </div>
                    </div>
                </div>

                <div className="mb-6">
                    <h3 className="font-bold mb-2">Bill To:</h3>
                    <p>{invoiceData.retailer}</p>
                    <p className="text-gray-600">Payment Terms: {invoiceData.paymentType}</p>
                </div>

                <div className="mb-6">
                    <table className="w-full">
                        <thead>
                            <tr className="border-b">
                                <th className="text-left py-2">Item</th>
                                <th className="text-right py-2">Qty</th>
                                <th className="text-right py-2">Unit Price</th>
                                <th className="text-right py-2">Amount</th>
                                <th className="text-right py-2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {invoiceData.items.map((item, index) => (
                                <tr key={index} className="border-b">
                                    <td className="py-2">{item.name}</td>
                                    <td className="text-right">{item.qty}</td>
                                    <td className="text-right">{item.unitPrice}</td>
                                    <td className="text-right">{item.amount.toFixed(2)}</td>
                                    <td className="text-right">
                                        <button
                                            onClick={() => removeItem(index)}
                                            className="text-red-500 hover:text-red-700"
                                        >
                                            Remove
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="flex justify-end">
                    <div className="w-1/3">
                        <div className="flex justify-between py-2">
                            <span>Subtotal:</span>
                            <span>{invoiceData.subtotal.toFixed(2)}</span>
                        </div>
                        <div className="flex justify-between py-2">
                            <span>Discount:</span>
                            <span>{invoiceData.discount.toFixed(2)}</span>
                        </div>
                        <div className="flex justify-between py-2 font-bold">
                            <span>Total:</span>
                            <span>{invoiceData.total.toFixed(2)}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default InvoiceGenerator;