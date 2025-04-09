import React, { useContext, useEffect, useState } from 'react'
import { NumericFormat } from 'react-number-format'
import { useRef } from 'react'
import jsPDF from 'jspdf'
import html2canvas from 'html2canvas'
import { jwtDecode } from 'jwt-decode'
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../Context/AuthContext';
import {
    getSellers,
    // saveRetailer,
    // editRetailer,
    // deleteRetailer,
} from "../../api/RetailerService";
import {
    getItems,
} from "../../api/ItemService"
import {
    saveBillItem,
} from "../../api/InvoiceItemService"
import {
    saveBill
} from "../../api/InvoiceService"
import {
    getUserID
} from "../../api/UserCredService"


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
    const [retailers, setRetailers] = useState([])
    const [items, setItems] = useState([])
    const { user } = useContext(AuthContext)
    const navigate = useNavigate();
    // const [userData, setUserData] = useState({
    //     userId: '',
    //     userName: ''
    // })
    // const [itemData, setItemData] = useState({
    //     itemId: '',
    //     itemName: ''
    // })
    const [retailerData, setRetailerData] = useState({
        retailerId: '',
        retailerName: '',
        limitCredit: null,
    })
    const [invoiceData, setInvoiceData] = useState({
        invoiceNo: '',
        issueDate: '',
        user: '',
        retailer: '',
        paymentType: '',
        items: [],
        billItemList: [],
        subtotal: 0,
        discount: 0,
        total: 0
    })

    const [currentItem, setCurrentItem] = useState({
        id: '',
        name: '',
        qty: '',
        unitPrice: '',
        amount: 0
    })

    useEffect(() => {
        fetchReatilers()
        fetchItems()
        // setUserLoggedIn()
        fetchUserID()
    }, []);

    const fetchItems = async () => {
        try {
            const response = await getItems();
            setItems(Array.isArray(response) ? response : response?.data || []);
        } catch (error) {
            console.error("Error fetching Items:", error);
            setItems([]);
        }
    }

    const fetchUserID = async () => {
        try {
            const response = await getUserID(user.username);
            setInvoiceData((prevData) => ({
                ...prevData,
                user: response
            }))
        } catch (error) {
            console.error("Error fetch UserID:", error);
        }
    }

    const fetchReatilers = async () => {
        try {
            const response = await getSellers();
            setRetailers(Array.isArray(response) ? response : response?.data || []);
            console.log(response)
        } catch (error) {
            console.error("Error fetching Retailers:", error);
            setRetailers([]);
        }
    }

    // const getUserName = () => {
    //     const token = localStorage.getItem("accessToken"); //should match the key used when storing the token -> localStorage.setItem("token", receivedJwtToken);
    //     if (token) {
    //         const decoded = jwtDecode(token)
    //         return decoded.username;
    //     }
    //     return ""
    // }

    // const setUserLoggedIn = async () => {
    //     console.log("useEffect started")
    //     // const loggedInUserName = getUserName();
    //     // const loggedInUserID = await getUserID(loggedInUserName)
    //     console.log("in useEffect and going to store value for user in invoice data")
    //     setInvoiceData((prevData) => ({
    //         ...prevData,
    //         user: 3 //loggedInUserID, // Set the logged-in user id
    //     }));
    // };

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
        // console.log(e.target.value)
        setInvoiceData(prev => ({
            ...prev,
            [name]: value
        }))
    }

    // const handleUserChange = (e) => {
    //     const { name, value } = e.target
    //     setUserData(prev => ({
    //         ...prev,
    //         [name]: value
    //     }))
    // }

    const handleRetailerChange = (e) => {
        const { name, value } = e.target
        handleInputChange(e)
        const selectedRetailer = retailers ? retailers.find(retailer => retailer.retailerId == value) : null
        // console.log(e.target.name)
        setRetailerData(prev => ({
            ...prev,
            retailerId: value ? value : prev.retailerId,
            retailerName: selectedRetailer ? selectedRetailer.retailerName : prev.retailerName,
            limitCredit: selectedRetailer ? selectedRetailer.limitCredit : null
        }))
        setInvoiceData(prev => ({
            ...prev,
            retailer: value // Ensure the retailer ID is set in invoiceData for submission
        }));
        // console.log(retailerData.retailerId)
    }

    const handleItemChange = (e) => {
        const { name, value } = e.target
        const selectedItem = items ? items.find(item => item.itemName == value) : null
        // console.log(selectedItem.itemCode)
        setCurrentItem(prev => ({
            ...prev,
            [name]: value,
            id: selectedItem && name === 'name' ? selectedItem.itemCode : prev.id,
            amount: name === 'qty' || name === 'unitPrice'
                ? (name === 'qty' ? value : prev.qty) * (name === 'unitPrice' ? value : prev.unitPrice)
                : prev.amount
        }))
        // console.log(currentItem.id)
    }

    const addItem = () => {
        if (currentItem.name && currentItem.qty && currentItem.unitPrice) {
            // console.log("current item: " + currentItem.amount + " " + currentItem.id)
            const selectedItem = {
                item: currentItem.id,
                // name: currentItem.name,
                quantity: currentItem.qty,
                // unitPrice: currentItem.unitPrice,
                totalValue: currentItem.amount
            }
            setInvoiceData(prev => ({
                ...prev,
                items: [...prev.items, currentItem],
                billItemList: [...prev.billItemList, selectedItem],
                subtotal: prev.subtotal + currentItem.amount,
                total: prev.subtotal + currentItem.amount - prev.discount
            }))
            setCurrentItem({
                id: '',
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

    // const handleItemSubmit = () => {
    //     // console.log("entered Item submit")
    //     // console.log(invoiceData.items)
    //     const billItems = invoiceData.items.map((billItem) => ({
    //         item: billItem.id,
    //         totalvalue: parseFloat(billItem.amount),
    //         quantity: parseInt(billItem.qty)
    //     }))
    //     console.log(billItems)
    //     try {
    //         const response = saveBillItem(billItems)
    //         console.log("BillItems saved successfully" + response)
    //     }
    //     catch (error) {
    //         console.error("Error: ", error)
    //     }
    // }

    const handleSubmit = async (e) => {
        e.preventDefault();

        // --- CREDIT LIMIT CHECK ---
        if (invoiceData.paymentType === 'CREDIT') {
            // Ensure retailer and their credit limit are loaded
            if (retailerData.retailerId === '' || retailerData.limitCredit === null || retailerData.limitCredit === undefined) {
                alert('Please select a retailer to check credit limit.');
                console.error("Retailer not selected or credit limit not available.", retailerData);
                return; // Stop submission
            }

            const currentBillTotal = parseFloat(invoiceData.total);
            const availableCredit = parseFloat(retailerData.limitCredit);

            if (isNaN(currentBillTotal) || isNaN(availableCredit)) {
                alert('Error calculating totals or credit limit. Please check values.');
                console.error("NaN detected in total or credit limit.", invoiceData.total, retailerData.limitCredit);
                return; // Stop submission
            }


            if (currentBillTotal > availableCredit) {
                alert(`Error: Bill total (${currentBillTotal.toFixed(2)}) exceeds available credit limit (${availableCredit.toFixed(2)}) for ${retailerData.retailerName}.`);
                console.error("Credit limit exceeded.");
                return; // Stop submission
            }
            console.log(`Credit check passed for ${retailerData.retailerName}. Bill Total: ${currentBillTotal}, Available Credit: ${availableCredit}`);
        }
        // --- END CREDIT LIMIT CHECK ---


        // Proceed with saving if check passed or payment type is not CREDIT
        try {
            const billData = {
                // Ensure field names match your backend Bill DTO or Entity expected by the saveBill endpoint
                // Assuming your backend expects Retailer ID as a string and User ID potentially as a number/string
                userID: String(invoiceData.user), // Ensure type consistency if backend expects String
                retailerID: invoiceData.retailer, // This should be the RetailerId string
                billCategory: invoiceData.paymentType,
                total: parseFloat(invoiceData.total),
                billItems: invoiceData.billItemList.map(item => ({
                    ...item,
                    // Ensure item ID is correctly formatted if needed (e.g., string/number)
                    // item: String(item.item), 
                })),
                // Add other necessary fields like issueDate if needed by backend
                // issueDate: invoiceData.issueDate || new Date().toISOString().split('T')[0], // Example: Default to today if empty
            };

            console.log("Submitting Bill Data:", billData); // Log data being sent

            // Make sure saveBill API endpoint and service handle the request correctly
            const response = await saveBill(billData);
            console.log("Bill is saved", response); // Log response from backend

            // Optional: Show success message
            alert('Invoice saved successfully!');
            if (response && response.billNO && response.date) {
                setInvoiceData(prev => ({
                    ...prev,
                    invoiceNo: response.billNO,
                    issueDate: response.date // Make sure your backend sends it in YYYY-MM-DD or ISO format
                }));
            }
            // navigate('/dashboard/sales/invoices'); // Navigate on success

        } catch (error) {
            console.error("Error saving Bill:", error);
            // Display specific error from backend if available
            if (error.response && error.response.data && error.response.data.message) {
                alert(`Error saving invoice: ${error.response.data.message}`);
            } else {
                alert('An error occurred while saving the invoice. Please check console for details.');
            }
        }
    };

    return (
        <div className="flex gap-6 p-6 min-h-screen bg-gray-50">
            {/* Left Side - Form */}
            <div className="w-1/2 p-6 bg-white rounded-lg shadow-md">
                <h2 className="text-2xl font-bold mb-6">Create Invoice</h2>

                <div className="space-y-4">
                    <div className="grid grid-cols-2 gap-4">
                        {/* <div>
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
                        </div> */}
                        <div className='flex justify-between w-full'>
                            <div className="w-1/2 pr-2">
                                <label className="block text-sm mb-1">User</label>
                                <Input
                                    name="user"
                                    value={user.username}
                                />
                            </div>
                            {/* <div className="w-1/2 pr-2">
                                <label className="block text-sm mb-1">User to be Notified</label>
                                <Select
                                    name="retailer"
                                    value={invoiceData.retailer}
                                    onChange={handleRetailerChange}
                                >
                                    <option value=''>Select User</option>
                                </Select>
                                <Input
                                    name="userNotify"
                                    value={}
                                />
                            </div> */}
                        </div>
                        {/* <label className="block text-sm mb-1">User</label>
                        <Select
                            name="user"
                            value={invoiceData.user}
                            onChange={handleUserChange}
                        >
                            <option value=''>Select User</option>
                            {retailers.map((retailer) => (
                                <option key={retailer.retailerId} value={retailer.retailerName}>
                                    {retailer.retailerName}
                                </option>
                            ))}
                        </Select> */}
                    </div>
                    <div>
                        <label className="block text-sm mb-1">Retailer</label>
                        <Select
                            name="retailer"
                            value={invoiceData.retailer}
                            onChange={handleRetailerChange}
                        >
                            <option value=''>Select Retailer</option>
                            {retailers.map((retailer) => (
                                <option key={retailer.retailerId} value={retailer.retailerId}>
                                    {retailer.retailerName}
                                </option>
                            ))}
                        </Select>
                        {retailerData.retailerId && ( // Only show if a retailer is selected
                            <div className="mt-2 text-sm text-gray-600"> {/* Added margin-top for spacing */}
                                Available Credit Limit:
                                <span className="font-semibold text-red-600 ml-1"> {/* Red text, bold, margin-left */}
                                    {/* Check if limitCredit is a valid number before formatting */}
                                    {(retailerData.limitCredit !== null && !isNaN(parseFloat(retailerData.limitCredit)))
                                        ? `Rs ${parseFloat(retailerData.limitCredit).toFixed(2)}`
                                        : ' N/A' /* Show N/A if null or not a number */
                                    }
                                </span>
                                {/* Optional: You can keep the warning logic if needed */}
                                {invoiceData.paymentType === 'CREDIT' && retailerData.limitCredit !== null && parseFloat(invoiceData.total) > parseFloat(retailerData.limitCredit) && (
                                    <p className="text-orange-600 text-xs mt-1">Warning: Current invoice total exceeds available credit.</p>
                                )}
                            </div>
                        )}
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
                            <Select
                                placeholder="Item name"
                                name="name"
                                value={currentItem.name}
                                onChange={handleItemChange}
                            >
                                <option value=''>Select Item</option>
                                {items.map((item) => (
                                    <option key={item.itemCode} value={item.itemName}>
                                        {item.itemName}
                                    </option>
                                ))}
                            </Select>
                            <Input
                                type="number"
                                placeholder="Qty"
                                name="qty"
                                value={currentItem.qty}
                                onChange={handleItemChange}
                            />
                            <Input
                                type="number"
                                step="0.01"
                                placeholder="Unit price"
                                name="unitPrice"
                                value={currentItem.unitPrice}
                                onChange={handleItemChange}
                            />
                            {/* <NumericFormat
                                value={currentItem.unitPrice}
                                thousandSeparator={true}
                                decimalScale={2}
                                fixedDecimalScale={true}
                                prefix={'Rs'}
                                placeholder="Unit price"
                                onValueChange={handleItemChange}
                                className="input-field"
                            /> */}
                            <Button onClick={addItem}>Add Item</Button>
                        </div>
                        <h3 className="text-lg font-semibold mb-4">Discount</h3>
                        <div>
                            <Input
                                type="number"
                                placeholder="%"
                                name="discount"
                                value={invoiceData.discount}
                                onChange={handleInputChange}
                                min="0"
                                max="100"
                                step="0.1"
                                className="w-40 px-2 py-1 border rounded text-left"
                            />
                        </div>
                        <div>
                            <Button
                                onClick={handleDownloadPDF}
                                className='bg-[#088612] text-white mt-3'
                            >
                                Download
                            </Button>
                            <Button
                                onClick={handleSubmit}
                                className='mr-2'
                                disabled={
                                    invoiceData.paymentType === 'CREDIT' &&
                                    retailerData.limitCredit !== null &&
                                    parseFloat(invoiceData.total) > parseFloat(retailerData.limitCredit)
                                }
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
                            <h2 className="font-bold">Jeewaloka Osusala,</h2>
                            <p className="text-gray-600">Dahamana,</p>
                            <p className="text-gray-600">Balangoda.</p>
                        </div>
                    </div>
                </div>

                <div className="mb-6">
                    <p className="font-bold mb-2">Bill To: {retailerData.retailerName}</p>
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
                            <span>{invoiceData.discount}%</span>
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