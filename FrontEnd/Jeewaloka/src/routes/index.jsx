import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Items from "../pages/Items/Items";
import GRN from "../pages/GRN/GRN";
import Dashboard from "../layouts/Layout";
import PurchaseOrder from "../pages/PurchaseOrder/PurchaseOrder";
import Sellers from "../pages/Sellers/Sellers";
import Supplier from "../pages/Supplier/Supplier";
import "../styles/index.css";
import LoginPage from "../pages/Login/LoginForm";


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
          <Route path="sales/sellers" element={<Sellers />} />
          <Route path="inventary/supplier" element={<Supplier />} />
          
        </Route>

      

      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
