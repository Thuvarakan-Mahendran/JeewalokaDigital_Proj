import { useState } from "react";

function Report() {
  const [reportType, setReportType] = useState("");

  const [secondReportType, setSecondReportType] = useState("");
  
  // Sample data fetching simulation (Replace with actual database fetch logic)
  const cashSale = "0";
  const creditSale = "0";
  const totalSale = "0";


  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold">Reports</h1>
      <div className="mt-4 p-6 w-full max-w-2xl border rounded-lg shadow-sm">
        <div className="flex space-x-4 mb-4">
          <select 
            className="border p-2 rounded w-64" 
            value={reportType} 
            onChange={(e) => setReportType(e.target.value)}
          >
            <option value="">Report type</option>
            <option value="sales">Sales Report</option>
            <option value="inventory">Inventory Report</option>
            <option value="finance">Financial Report</option>
          </select>
          <select 
            className="border p-2 rounded w-64" 
            value={secondReportType} 
            onChange={(e) => setSecondReportType(e.target.value)}
          >
            <option value="">Report Duration</option>
            <option value="monthly">Monthly Report</option>
            <option value="quarterly">Quarterly Report</option>
            <option value="yearly">Yearly Report</option>
          </select>
        </div>
        <div className="space-y-4">
          <div className="flex justify-between items-center">
            <span>Total cash sale by now:</span>
            <input className="border p-2 rounded w-32" value={cashSale} readOnly />
          </div>
          <div className="flex justify-between items-center">
            <span>Total credit sale by now:</span>
            <input className="border p-2 rounded w-32" value={creditSale} readOnly />
          </div>
          <div className="flex justify-between items-center">
            <span>Total sale by now:</span>
            <input className="border p-2 rounded w-32" value={totalSale} readOnly />
          </div>
        </div>
      </div>
    </div>
  )
}


