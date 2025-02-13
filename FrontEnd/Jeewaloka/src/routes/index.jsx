import { BrowserRouter, Routes, Route } from "react-router-dom";
import Items from "../pages/Items/Items";
import GRN from "../pages/GRN/GRN";
import Dashboard from "../layouts/Layout";
import PurchaseOrder from "../pages/PurchaseOrder/PurchaseOrder";
import Supplier from "../pages/Supplier/Supplier";
import "../styles/index.css";

const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Dashboard />}>
          <Route path="inventary/items" element={<Items />} />
          <Route path="inventary/grn" element={<GRN />} />
          <Route path="inventary/purchaseorder" element={<PurchaseOrder />} />
          <Route path="inventary/supplier" element={<Supplier/>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
export default AppRoutes;
