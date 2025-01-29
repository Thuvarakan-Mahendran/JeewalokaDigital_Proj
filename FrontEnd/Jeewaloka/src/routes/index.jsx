import { BrowserRouter, Routes, Route } from "react-router-dom";
import Items from "../pages/Items/Items";
import Dashboard from "../layouts/Layout";
import "../styles/index.css";

const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Dashboard />}>
          <Route path="inventary/items" element={<Items />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
export default AppRoutes;
