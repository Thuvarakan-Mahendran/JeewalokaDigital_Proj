import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Items from "../pages/Items/Items";
import GRN from "../pages/GRN/ViewGRN";
import Dashboard from "../layouts/Layout";
import PurchaseOrder from "../pages/PurchaseOrder/PurchaseOrder";
import Sellers from "../pages/Sellers/Sellers";
import Supplier from "../pages/Supplier/Supplier";
import CreateGRN from "../pages/GRN/CreateGRN";
import "../styles/index.css";
import LoginPage from "../pages/Login/LoginForm";
import Invoices from "../pages/Invoices/Invoices";
import InvoiceGenerator from "../pages/Invoices/InvoiceGenerator";
import Users from "../pages/User/Users";
import Report from "../pages/Reports/Report";
import DashboardPage from "../pages/DashboardPage/DashboardForm";



import GRRN from "../pages/GRRN/GRRN";
import SellerReturn from "../pages/SellersReturn/SellerReturn";
import SellerOrder from "../pages/SellerOrder/SellerOrder";
import ProtectedRoute from "./ProtectedRoute";


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
          <Route path="inventary/items" element={
            <ProtectedRoute roles={['ROLE_ADMIN']}>
              <Items />
            </ProtectedRoute>
          } />
          <Route path="inventary/grn" element={<GRN />} />
          <Route path="inventary/purchaseorder" element={<PurchaseOrder />} />
          <Route path="inventary/grrn" element={<GRRN />} />
          <Route path="sales/sellers" element={<Sellers />} />
          <Route path="inventary/supplier" element={<Supplier />} />

          <Route path="inventary/grn/creategrn" element={<CreateGRN />} />
          {/* <Route path="sales/invoices" element={<Invoices />} /> */}
          {/* <Route path="sales/invoices/createInvoice" element={<InvoiceGenerator />} /> */}
          {/* <Route path="users" element={<Users />} /> */}
          <Route path="users" element={
            <ProtectedRoute roles={['ROLE_ADMIN']}>
              <Users />
            </ProtectedRoute>
          } />
          <Route path="unauthorized" element={<h1>Unauthorized</h1>} />


          <Route path="Reports/ReportsPage" element={<Report />} />
          <Route path="sales/invoices" element={<Invoices />} />
          <Route path="sales/invoices/createInvoice" element={<InvoiceGenerator />} />
          {/* <Route path="users" element={<Users />} /> */}
          <Route path="dashboardform" element={<DashboardPage />} />
          <Route path="sales/sellersreturns" element={<SellerReturn />} />
          <Route path="sales/sellerorder" element={<SellerOrder />} />


        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
