import React, { useState, useEffect } from "react";

const DashboardPage = () => {
  const [cashSales, setCashSales] = useState(0);
  const [creditSales, setCreditSales] = useState(0);
  const [totalSales, setTotalSales] = useState(0);
  const [dateTime, setDateTime] = useState(new Date());

  useEffect(() => {
    setTotalSales(cashSales + creditSales);
  }, [cashSales, creditSales]);

  useEffect(() => {
    const timer = setInterval(() => setDateTime(new Date()), 1000);
    return () => clearInterval(timer);
  }, []);

  return (
    <div className="flex gap-6 p-6 bg-gray-100 min-h-screen justify-center items-center">
      {/* Sales Details Card */}
      <div className="bg-white p-6 rounded-2xl shadow-md w-72">
        <p className="block mb-2 font-semibold">cash sales this month: {cashSales}</p>
        <p className="block mb-2 font-semibold">credit sales this month: {creditSales}</p>
        <p className="block mb-2 font-semibold">total sales this month: {totalSales}</p>
      </div>
      
      {/* Date & Time Card */}
      <div className="bg-white p-6 rounded-2xl shadow-md w-64 text-center">
        <p className="text-lg font-semibold">{dateTime.toDateString()}</p>
        <p className="text-lg font-semibold mt-2">{dateTime.toLocaleTimeString()}</p>
      </div>
      
      {/* Profile Card */}
      <div className="bg-white p-6 rounded-2xl shadow-md w-64 text-center">
        <div className="flex justify-center">
          <img
            className="w-12 h-12 rounded-full"
            src="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
            alt="user photo"
          />
        </div>
        <p className="font-semibold mt-2">Neil Sims</p>
        <p className="text-gray-500">neil.sims@flowbite.com</p>
      </div>
    </div>
  );
};

export default DashboardPage;
