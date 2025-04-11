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

const ROLES = {
  ADMIN: 'ROLE_ADMIN',
  MANAGER: 'ROLE_MANAGER',
  CASHIER: 'ROLE_CASHIER',
};

const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Redirect '/' to '/login' */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* Login Page */}
        <Route path="/login" element={<LoginPage />} />

        <Route path="unauthorized" element={<h1>Unauthorized</h1>} />

        {/* Protected Dashboard Route */}
        <Route path="/dashboard" element={<Dashboard />}>
          <Route path="dashboardform" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER, ROLES.CASHIER]}>
              <DashboardPage />
            </ProtectedRoute>
          } />
          {/* <Route path="/system" element={<DashboardPage />}> */}
          <Route path="inventary/items" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER]}>
              <Items />
            </ProtectedRoute>
          } />
          <Route path="users" element={
            <ProtectedRoute roles={[ROLES.ADMIN]}>
              <Users />
            </ProtectedRoute>
          } />
          <Route path="inventary/grn" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER]}>
              <GRN />
            </ProtectedRoute>
          } />
          <Route path="inventary/purchaseorder" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER]}>
              <PurchaseOrder />
            </ProtectedRoute>
          } />
          <Route path="inventary/grrn" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER]}>
              <GRRN />
            </ProtectedRoute>
          } />
          <Route path="inventary/supplier" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER]}>
              <Supplier />
            </ProtectedRoute>
          } />
          <Route path="inventary/grn/creategrn" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.MANAGER]}>
              <CreateGRN />
            </ProtectedRoute>
          } />

          {/* <Route path="sales/invoices" element={<Invoices />} /> */}
          {/* <Route path="sales/invoices/createInvoice" element={<InvoiceGenerator />} /> */}
          {/* <Route path="users" element={<Users />} /> */}


          <Route path="sales/invoices" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.CASHIER]}>
              <Invoices />
            </ProtectedRoute>
          } />
          <Route path="sales/sellers" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.CASHIER]}>
              <Sellers />
            </ProtectedRoute>
          } />
          <Route path="sales/invoices/createInvoice" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.CASHIER]}>
              <InvoiceGenerator />
            </ProtectedRoute>
          } />
          {/* <Route path="users" element={<Users />} /> */}
          <Route path="sales/sellersreturns" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.CASHIER]}>
              <SellerReturn />
            </ProtectedRoute>
          } />
          <Route path="sales/sellerorder" element={
            <ProtectedRoute roles={[ROLES.ADMIN, ROLES.CASHIER]}>
              <SellerOrder />
            </ProtectedRoute>
          } />

          <Route path="Reports/ReportsPage" element={<Report />} />

          {/* <Route path="Reports/ReportsPage" element={
            <ProtectedRoute>
              <Report />
            </ProtectedRoute>
          } /> */}
          {/* <Route path="unauthorized" element={<h1>Unauthorized</h1>} /> */}
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
