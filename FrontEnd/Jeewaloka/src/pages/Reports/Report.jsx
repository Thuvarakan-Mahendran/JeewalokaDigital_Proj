import { useState, useEffect } from "react";
import { Bar, Pie, Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, PointElement, LineElement } from 'chart.js';
import { getBills } from "../../api/InvoiceService"; // Adjust the import path as necessary

// Register ChartJS components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement
);

function Report() {
  const [reportType, setReportType] = useState("sales");
  const [timePeriod, setTimePeriod] = useState("monthly");
  const [loading, setLoading] = useState(false);
  const [reportData, setReportData] = useState(null);
  const [bills, setBills] = useState([]);

  useEffect(() => {
    const fetchBills = async () => {
      setLoading(true);
      try {
        const billsData = await getBills();
        setBills(billsData);
        processBillData(billsData, reportType, timePeriod);
      } catch (error) {
        console.error('Error fetching bills:', error);
        const data = generateSampleData(reportType);
        setReportData(data);
      } finally {
        setLoading(false);
      }
    };

    fetchBills();
  }, [reportType, timePeriod]);

  const processBillData = (bills, type) => {
    if (!bills || bills.length === 0) {
      const data = generateSampleData(type);
      setReportData(data);
      return;
    }

    const monthlyData = {};
    let totalCash = 0;
    let totalCredit = 0;

    bills.forEach(bill => {
      const date = new Date(bill.date);
      const monthYear = `${date.getFullYear()}-${date.getMonth() + 1}`;
      
      if (bill.billCategory === 'CASH') {
        totalCash += bill.total || 0;
      } else {
        totalCredit += bill.total || 0;
      }

      if (!monthlyData[monthYear]) {
        monthlyData[monthYear] = { cash: 0, credit: 0 };
      }
      
      if (bill.billCategory === 'CASH') {
        monthlyData[monthYear].cash += bill.total || 0;
      } else {
        monthlyData[monthYear].credit += bill.total || 0;
      }
    });

    const months = Object.keys(monthlyData).sort();

    if (type === "sales") {
      setReportData({
        cashSale: totalCash.toLocaleString('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 }),
        creditSale: totalCredit.toLocaleString('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 }),
        totalSale: (totalCash + totalCredit).toLocaleString('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 }),
        chartData: {
          labels: months,
          datasets: [
            {
              label: 'Cash Sales',
              data: months.map(month => monthlyData[month].cash),
              backgroundColor: 'rgba(54, 162, 235, 0.5)',
            },
            {
              label: 'Credit Sales',
              data: months.map(month => monthlyData[month].credit),
              backgroundColor: 'rgba(255, 99, 132, 0.5)',
            }
          ],
        }
      });
    } else if (type === "inventory") {
      const data = generateSampleData(type);
      setReportData(data);
    } else if (type === "finance") {
      setReportData({
        chartData: {
          labels: months,
          datasets: [
            {
              label: 'Revenue',
              data: months.map(month => monthlyData[month].cash + monthlyData[month].credit),
              borderColor: 'rgb(75, 192, 192)',
              backgroundColor: 'rgba(75, 192, 192, 0.5)',
              tension: 0.1
            }
          ],
        }
      });
    }
  };

  const generateSampleData = (type) => {
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
    const categories = ['Foods', 'Clothing', 'Groceries', 'Furniture'];
    
    if (type === "sales") {
      return {
        cashSale: "12,450.00",
        creditSale: "8,720.00",
        totalSale: "21,170.00",
        chartData: {
          labels: months,
          datasets: [
            {
              label: 'Cash Sales',
              data: months.map(() => Math.floor(Math.random() * 5000) + 1000),
              backgroundColor: 'rgba(54, 162, 235, 0.5)',
            },
            {
              label: 'Credit Sales',
              data: months.map(() => Math.floor(Math.random() * 3000) + 500),
              backgroundColor: 'rgba(255, 99, 132, 0.5)',
            }
          ],
        }
      };
    } else if (type === "inventory") {
      return {
        chartData: {
          labels: categories,
          datasets: [
            {
              label: 'Inventory Levels',
              data: categories.map(() => Math.floor(Math.random() * 100) + 20),
              backgroundColor: [
                'rgba(255, 99, 132, 0.5)',
                'rgba(54, 162, 235, 0.5)',
                'rgba(255, 206, 86, 0.5)',
                'rgba(75, 192, 192, 0.5)',
              ],
              borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
              ],
              borderWidth: 1,
            }
          ],
        }
      };
    } else if (type === "finance") {
      return {
        chartData: {
          labels: months,
          datasets: [
            {
              label: 'Revenue',
              data: months.map(() => Math.floor(Math.random() * 10000) + 5000),
              borderColor: 'rgb(75, 192, 192)',
              backgroundColor: 'rgba(75, 192, 192, 0.5)',
              tension: 0.1
            },
            {
              label: 'Expenses',
              data: months.map(() => Math.floor(Math.random() * 8000) + 3000),
              borderColor: 'rgb(255, 99, 132)',
              backgroundColor: 'rgba(255, 99, 132, 0.5)',
              tension: 0.1
            }
          ],
        }
      };
    }
  };

  const renderChart = () => {
    if (!reportData || !reportData.chartData) {
      return (
        <div className="flex justify-center items-center h-64 text-gray-500">
          No data available for the selected criteria
        </div>
      );
    }

    switch (reportType) {
      case "sales":
        return <Bar data={reportData.chartData} options={{
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: 'Sales Report',
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  return `${context.dataset.label}: LKR ${context.raw.toFixed(2)}`;
                }
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                callback: function(value) {
                  return 'LKR ' + value;
                }
              }
            }
          }
        }} />;
      case "inventory":
        return <Pie data={reportData.chartData} options={{
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: 'Inventory Report',
            },
          },
        }} />;
      case "finance":
        return <Line data={reportData.chartData} options={{
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: 'Financial Report',
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  return `Revenue: LKR ${context.raw.toFixed(2)}`;
                }
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                callback: function(value) {
                  return 'LKR ' + value;
                }
              }
            }
          }
        }} />;
      default:
        return null;
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold">Reports Dashboard</h1>
      <div className="mt-4 p-6 w-full max-w-5xl border rounded-lg shadow-sm">
        <div className="flex space-x-4 mb-6">
          <select
            className="border p-2 rounded w-64"
            value={reportType}
            onChange={(e) => setReportType(e.target.value)}
          >
            <option value="sales">Sales Report</option>
            <option value="inventory">Inventory Report</option>
            <option value="finance">Financial Report</option>
          </select>
          <select
            className="border p-2 rounded w-64"
            value={timePeriod}
            onChange={(e) => setTimePeriod(e.target.value)}
          >
            <option value="monthly">Monthly Report</option>
            <option value="quarterly">Quarterly Report</option>
            <option value="yearly">Yearly Report</option>
          </select>
        </div>

        {loading ? (
          <div className="flex justify-center items-center h-64">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        ) : (
          <>
            {reportType === "sales" && reportData && (
              <div className="space-y-4 mb-6">
                <div className="flex justify-between items-center">
                  <span className="font-medium">Total cash sale:</span>
                  <input className="border p-2 rounded w-32 text-right" value={`LKR ${reportData.cashSale}`} readOnly />
                </div>
                <div className="flex justify-between items-center">
                  <span className="font-medium">Total credit sale:</span>
                  <input className="border p-2 rounded w-32 text-right" value={`LKR ${reportData.creditSale}`} readOnly />
                </div>
                <div className="flex justify-between items-center">
                  <span className="font-medium">Total sale:</span>
                  <input className="border p-2 rounded w-32 text-right font-semibold" value={`LKR ${reportData.totalSale}`} readOnly />
                </div>
              </div>
            )}

            <div className="h-96">
              {renderChart()}
            </div>

            <div className="mt-4 text-sm text-gray-500">
              <p>Report period: {timePeriod.charAt(0).toUpperCase() + timePeriod.slice(1)}</p>
              <p>Generated on: {new Date().toLocaleDateString()}</p>
              {bills.length > 0 && <p>Total bills processed: {bills.length}</p>}
            </div>
          </>
        )}
      </div>
    </div>
  )
}

export default Report;