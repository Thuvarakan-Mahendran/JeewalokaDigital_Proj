import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Items from "../pages/Items/Items";
import GRN from "../pages/GRN/GRN";
import Dashboard from "../layouts/Layout";
import PurchaseOrder from "../pages/PurchaseOrder/PurchaseOrder";
import Sellers from "../pages/Sellers/Sellers";
import Supplier from "../pages/Supplier/Supplier";
import "../styles/index.css";
<<<<<<< Updated upstream
import LoginPage from "../pages/Login/LoginForm";
<<<<<<< HEAD
import DashboardPage from "../pages/DashboardPage/DashboardForm";
=======
>>>>>>> f8fa023220e2149f0784891f5fecdd2fe1757822

import { Rule } from "postcss";
import Invoices from "../pages/Invoices/Invoices";
import InvoiceGenerator from "../pages/Invoices/InvoiceGenerator";
import Users from "../pages/User/Users";
import GRRN from "../pages/GRRN/GRRN";
import SellerReturn from "../pages/SellersReturn/SellerReturn";
import SellerOrder from "../pages/SellerOrder/SellerOrder";


const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Redirect '/' to '/login' */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* Login Page */}
        <Route path="/login" element={<LoginPage />} />

        {/* Protected Dashboard Route */}
        <Route path="/dashboard" element={<Dashboard />}>
          <Route path="inventary/items" element={<Items />} />
          <Route path="inventary/grn" element={<GRN />} />
          <Route path="inventary/purchaseorder" element={<PurchaseOrder />} />
          <Route path="inventary/grrn" element={<GRRN />} />
          <Route path="sales/sellers" element={<Sellers />} />
          <Route path="inventary/supplier" element={<Supplier />} />
<<<<<<< Updated upstream
          
=======
          <Route path="sales/invoices" element={<Invoices />} />
          <Route path="sales/invoices/createInvoice" element={<InvoiceGenerator />} />
          <Route path="users" element={<Users />} />
>>>>>>> Stashed changes
        </Route>

      

      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
